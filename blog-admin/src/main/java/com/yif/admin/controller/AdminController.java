package com.yif.admin.controller;

import com.yif.admin.model.params.PageParam;
import com.yif.admin.pojo.Permission;
import com.yif.admin.service.PermissionSerivce;
import com.yif.admin.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private PermissionSerivce permissionSerivce;

    @PostMapping("/permission/permissionList")
    public ResultVo listPermission(@RequestBody PageParam pageParam) {
        return permissionSerivce.listPermission(pageParam);
    }

    @PostMapping("permission/add")
    public ResultVo add(@RequestBody Permission permission){
        return permissionSerivce.add(permission);
    }

    @PostMapping("permission/update")
    public ResultVo update(@RequestBody Permission permission){
        return permissionSerivce.update(permission);
    }

    @GetMapping("permission/delete/{id}")
    public ResultVo delete(@PathVariable("id") Long id){
        return permissionSerivce.delete(id);
    }
}
