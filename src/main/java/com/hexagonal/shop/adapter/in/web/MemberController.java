package com.hexagonal.shop.adapter.in.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexagonal.shop.adapter.in.web.dto.ReqMemberDto;
import com.hexagonal.shop.adapter.in.web.dto.RespMemberDto;
import com.hexagonal.shop.application.port.in.MemberUsecase;
import com.hexagonal.shop.common.exception.ShopException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shop")
public class MemberController {
    
    private final MemberUsecase memberUsecase;

    @PostMapping(path = "/member")
    public ResponseEntity<?> join(@Valid @RequestBody ReqMemberDto reqMemberDto) throws ShopException {
        
        memberUsecase.addMember(reqMemberDto);

        return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/member/{id}")
    public ResponseEntity<?> removeMember(@PathVariable("id") String id) throws ShopException {

        memberUsecase.removeMember(id);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @GetMapping(path = "/member/{id}")
    public ResponseEntity<RespMemberDto> getMemberById(@PathVariable("id") String id) throws ShopException{

        return new ResponseEntity<RespMemberDto>(memberUsecase.getMemberById(id), HttpStatus.OK);
    }

    @PutMapping(path = "/member/{id}")
    public ResponseEntity<?> updateMember(@PathVariable("id") String id, @Valid @RequestBody ReqMemberDto reqMemberDto) throws ShopException {

        memberUsecase.updateMember(id, reqMemberDto);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @GetMapping(path = "/members")
    public ResponseEntity<List<RespMemberDto>> getAllMember() {

        return new ResponseEntity<List<RespMemberDto>>(memberUsecase.getAllMember(), HttpStatus.OK);
    }
}
