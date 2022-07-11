package com.yif.admin.service;

import com.yif.admin.model.params.PageParam;
import com.yif.admin.pojo.Permission;
import com.yif.admin.vo.ResultVo;

public interface PermissionSerivce {
    ResultVo listPermission(PageParam pageParam);

    ResultVo add(Permission permission);

    ResultVo update(Permission permission);

    ResultVo delete(Long id);
}
