package org.example.service;

import org.example.dto.BalanceResponse;
import org.example.dto.ExpenseDto;
import org.example.dto.GroupDto;
import org.example.entity.BillGroup;
import org.example.entity.Customer;
import org.example.entity.Expense;
import org.example.repository.BillGroupRepository;
import org.example.repository.CustomerRepository;
import org.example.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class BillGroupService {

    @Autowired
    private BillGroupRepository groupRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // ---------- 1. Group banao ----------
    public GroupDto createGroup(String email, String name) {
        if (name == null || name.isBlank()) {
            throw new RuntimeException("Group ka naam zaroori hai");
        }
        Customer me = findCustomer(email);

        BillGroup group = new BillGroup();
        group.setName(name);
        group.setCreatedBy(me);
        group.getMembers().add(me);
        return toGroupDto(groupRepository.save(group));
    }

    // ---------- 2. Mere groups ----------
    public List<GroupDto> getMyGroups(String email) {
        Customer me = findCustomer(email);
        List<GroupDto> result = new ArrayList<>();
        for (BillGroup group : groupRepository.findByMembersId(me.getId())) {
            result.add(toGroupDto(group));
        }
        return result;
    }

    // ---------- 3. Member jodo ----------
    public GroupDto addMember(String email, Long groupId, String memberEmail) {
        Customer me = findCustomer(email);
        BillGroup group = findGroup(groupId);
        checkMember(group, me);

        Customer newMember = customerRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new RuntimeException("Is email ka customer nahi mila"));
        if (isMember(group, newMember)) {
            throw new RuntimeException("Ye customer pehle se group mein hai");
        }
        group.getMembers().add(newMember);
        return toGroupDto(groupRepository.save(group));
    }

    // ---------- 4. Kharcha add karo ----------
    public ExpenseDto addExpense(String email, Long groupId, ExpenseDto dto) {
        Customer me = findCustomer(email);
        BillGroup group = findGroup(groupId);
        checkMember(group, me);

        if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount 0 se zyada hona chahiye");
        }

        Expense expense = new Expense();
        expense.setGroup(group);
        expense.setPaidBy(me);
        expense.setDescription(dto.getDescription());
        expense.setAmount(dto.getAmount());
        expense = expenseRepository.save(expense);

        ExpenseDto response = new ExpenseDto();
        response.setId(expense.getId());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount());
        response.setPaidBy(label(me));
        response.setCreatedAt(expense.getCreatedAt());
        return response;
    }

    // ---------- 5. Hisaab ----------
    public BalanceResponse getBalance(String email, Long groupId) {
        Customer me = findCustomer(email);
        BillGroup group = findGroup(groupId);
        checkMember(group, me);

        List<Expense> expenses = expenseRepository.findByGroupId(groupId);
        List<Customer> members = group.getMembers();

        BigDecimal total = BigDecimal.ZERO;
        Map<Long, BigDecimal> paidMap = new HashMap<>();
        for (Expense expense : expenses) {
            total = total.add(expense.getAmount());
            paidMap.merge(expense.getPaidBy().getId(), expense.getAmount(), BigDecimal::add);
        }

        // har member ka hissa = total / members ki ginti
        BigDecimal share = total.divide(BigDecimal.valueOf(members.size()), 2, RoundingMode.HALF_UP);

        // balance = jitna diya - apna hissa (plus = lena hai, minus = dena hai)
        Map<String, BigDecimal> balances = new LinkedHashMap<>();
        for (Customer member : members) {
            BigDecimal paid = paidMap.getOrDefault(member.getId(), BigDecimal.ZERO);
            balances.put(label(member), paid.subtract(share));
        }

        BalanceResponse response = new BalanceResponse();
        response.setTotalExpense(total);
        response.setBalances(balances);
        response.setSettlements(buildSettlements(balances));
        return response;
    }

    // "kaun kisko kitna dega" ki seedhi list
    private List<String> buildSettlements(Map<String, BigDecimal> balances) {
        List<Map.Entry<String, BigDecimal>> creditors = new ArrayList<>();
        List<Map.Entry<String, BigDecimal>> debtors = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : balances.entrySet()) {
            if (entry.getValue().compareTo(BigDecimal.ZERO) > 0) {
                creditors.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
            } else if (entry.getValue().compareTo(BigDecimal.ZERO) < 0) {
                debtors.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().negate()));
            }
        }

        List<String> settlements = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < debtors.size() && j < creditors.size()) {
            Map.Entry<String, BigDecimal> debtor = debtors.get(i);
            Map.Entry<String, BigDecimal> creditor = creditors.get(j);

            BigDecimal pay = debtor.getValue().min(creditor.getValue());
            if (pay.compareTo(new BigDecimal("0.01")) >= 0) {
                settlements.add(debtor.getKey() + " dega " + creditor.getKey() + " ko Rs." + pay);
            }

            debtor.setValue(debtor.getValue().subtract(pay));
            creditor.setValue(creditor.getValue().subtract(pay));
            if (debtor.getValue().compareTo(BigDecimal.ZERO) == 0) {
                i++;
            }
            if (creditor.getValue().compareTo(BigDecimal.ZERO) == 0) {
                j++;
            }
        }
        return settlements;
    }

    // ---------- chhote helper methods ----------
    private Customer findCustomer(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    private BillGroup findGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
    }

    private boolean isMember(BillGroup group, Customer customer) {
        return group.getMembers().stream()
                .anyMatch(m -> m.getId().equals(customer.getId()));
    }

    private void checkMember(BillGroup group, Customer customer) {
        if (!isMember(group, customer)) {
            throw new RuntimeException("Aap is group ke member nahi ho");
        }
    }

    private String label(Customer customer) {
        return customer.getName() + " (" + customer.getEmail() + ")";
    }

    private GroupDto toGroupDto(BillGroup group) {
        GroupDto dto = new GroupDto();
        dto.setId(group.getId());
        dto.setName(group.getName());
        List<String> members = new ArrayList<>();
        for (Customer member : group.getMembers()) {
            members.add(label(member));
        }
        dto.setMembers(members);
        return dto;
    }
}
