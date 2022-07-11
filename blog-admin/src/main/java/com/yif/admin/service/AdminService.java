package com.yif.admin.service;

import com.yif.admin.pojo.Admin;
import com.yif.admin.pojo.Permission;

import java.util.List;

public interface AdminService {
    Admin findAdminByUsername(String username);

    List<Permission> findPermissionByAdminId(Long adminId);
}
