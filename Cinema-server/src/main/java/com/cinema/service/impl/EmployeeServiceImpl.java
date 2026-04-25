package com.cinema.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.cinema.constant.MessageConstant;
import com.cinema.constant.PasswordConstant;
import com.cinema.constant.StatusConstant;
import com.cinema.context.BaseContext;
import com.cinema.dto.EmployeeDTO;
import com.cinema.dto.EmployeeLoginDTO;
import com.cinema.dto.EmployeePageQueryDTO;
import com.cinema.entity.Employee;
import com.cinema.exception.AccountLockedException;
import com.cinema.exception.AccountNotFoundException;
import com.cinema.exception.PasswordErrorException;
import com.cinema.mapper.EmployeeMapper;
import com.cinema.result.PageResult;
import com.cinema.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 新增员工
     *
     * @return
     */

    @Transactional
    public void save(EmployeeDTO employeeDTO) {
        System.out.println("当前的id�? + Thread.currentThread().getId());
        Employee employee = new Employee();
        //属性拷�?
        BeanUtils.copyProperties(employeeDTO, employee);
        //设置账号状�?

        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //设置创建人和修改人id

        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.insert(employee);
    }

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数�?
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定�?
        if (employee == null) {
            //账号不存�?
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁�?
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对�?
        return employee;
    }

    /**
     * 启用禁用员工账号
     *
     * @param status
     * @param id
     */
    @Override
    public void StartOrStop(Integer status, long id) {
        //Employee employee = new Employee();第一种写�?
        //employee.setStatus(status);
        //employee.setId(id);
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.update(employee);
    }

    /**
     * 根据id搜索员工
     * @param id
     * @return
     */
    @Override
    public Employee getById(Long id) {
        Employee employee = employeeMapper.getById(id);
        employee.setPassword("****");
        return employee;
    }

    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        employee.setCreateTime(LocalDateTime.now());
        employee.setCreateUser(BaseContext.getCurrentId());
        employeeMapper.update(employee);

    }

    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("分页查询参数：page={}, pageSize={}, name={}",
                employeePageQueryDTO.getPage(),
                employeePageQueryDTO.getPageSize(),
                employeePageQueryDTO.getName());

        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);

        long total = page.getTotal();
        List<Employee> records = page.getResult();

        log.info("查询结果：total={}, records.size={}", total, records.size());

        return new PageResult(total, records);
    }


}
