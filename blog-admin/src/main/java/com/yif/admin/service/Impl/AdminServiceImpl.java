package com.yif.admin.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yif.admin.mapper.AdminMapper;
import com.yif.admin.pojo.Admin;
import com.yif.admin.pojo.Permission;
import com.yif.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin findAdminByUsername(String username) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername,username);
        queryWrapper.last("limit 1");
        Admin admin = this.adminMapper.selectOne(queryWrapper);
        return admin;
    }

    @Override
    public List<Permission> findPermissionByAdminId(Long adminId) {
        return adminMapper.findPermissionByAdminId(adminId);
    }
}
