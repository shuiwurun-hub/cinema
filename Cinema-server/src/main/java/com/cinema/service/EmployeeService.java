package com.cinema.service;

import com.cinema.dto.EmployeeDTO;
import com.cinema.dto.EmployeeLoginDTO;
import com.cinema.dto.EmployeePageQueryDTO;
import com.cinema.entity.Employee;
import com.cinema.result.PageResult;

public interface EmployeeService {
    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 新增员工
     * @return
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     */
    void StartOrStop(Integer status, long id);

    Employee getById(Long id);

    void update(EmployeeDTO employeeDTO);
}
