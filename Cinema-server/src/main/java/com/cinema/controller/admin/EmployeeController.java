package com.cinema.controller.admin;

import com.cinema.constant.JwtClaimsConstant;
import com.cinema.dto.EmployeeDTO;
import com.cinema.dto.EmployeeLoginDTO;
import com.cinema.dto.EmployeePageQueryDTO;
import com.cinema.entity.Employee;
import com.cinema.properties.JwtProperties;
import com.cinema.result.PageResult;
import com.cinema.result.Result;
import com.cinema.service.EmployeeService;
import com.cinema.utils.JwtUtil;
import com.cinema.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退�?
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }
@PostMapping
@ApiOperation("新增员工")

    public Result save(@RequestBody EmployeeDTO employeeDTO){
    System.out.println("当前的id�?+ Thread.currentThread().getId());
        log.info("新增员工{}",employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }
    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询，参数为：{}",employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);

        return Result.success(pageResult);
    }
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号")
    public Result StartOrStop(@PathVariable Integer status, @RequestParam Long id){
        log.info("启用禁用员工账号：{},{}",status,id);
        employeeService.StartOrStop(status,id);
        return Result.success();
    }
    /**
     *
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工")
    public Result<Employee> getByID(@PathVariable Long id){
        log.info("根据id查询员工：{}",id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }
    @PutMapping
    @ApiOperation("更新员工信息")
    public Result<Employee> update(@RequestBody EmployeeDTO employeeDTO){
        log.info("更新员工信息：{}",employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

}
