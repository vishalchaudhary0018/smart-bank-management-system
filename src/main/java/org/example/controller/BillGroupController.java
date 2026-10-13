package org.example.controller;

import org.example.dto.BalanceResponse;
import org.example.dto.ExpenseDto;
import org.example.dto.GroupDto;
import org.example.dto.MemberRequest;
import org.example.service.BillGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class BillGroupController {

    @Autowired
    private BillGroupService billGroupService;

    @PostMapping
    public GroupDto createGroup(@RequestBody GroupDto dto, Authentication authentication) {
        return billGroupService.createGroup(authentication.getName(), dto.getName());
    }

    @GetMapping
    public List<GroupDto> myGroups(Authentication authentication) {
        return billGroupService.getMyGroups(authentication.getName());
    }

    @PostMapping("/{id}/members")
    public GroupDto addMember(@PathVariable Long id, @RequestBody MemberRequest request,
                              Authentication authentication) {
        return billGroupService.addMember(authentication.getName(), id, request.getEmail());
    }

    @PostMapping("/{id}/expenses")
    public ExpenseDto addExpense(@PathVariable Long id, @RequestBody ExpenseDto dto,
                                 Authentication authentication) {
        return billGroupService.addExpense(authentication.getName(), id, dto);
    }

    @GetMapping("/{id}/balance")
    public BalanceResponse balance(@PathVariable Long id, Authentication authentication) {
        return billGroupService.getBalance(authentication.getName(), id);
    }
}
