package per.zdy.socketexchangeclientcp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import per.zdy.socketexchangeclientcp.domain.Pojo.User;
import per.zdy.socketexchangeclientcp.service.UserService;


import java.time.LocalDate;
import java.util.List;

/**
 * @Description:
 * POST - Create a new resource
 * GET - Read a resource
 * PUT - Update an existing resource
 * DELETE - Delete a resource
 * Tomcat by default is not enabled for HTTP PUT command.
 * 只要让地址栏的参数可以传入函数里面，就能执行修改、新增、删除操作，method用RequestMethod.GET即可
 * 用PUT、POST、DELETE会报405错误，因为输入到地址栏默认使用GET方法
 * 注解@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)主要是解决请求日期无法转成LocalDate的问题
 * @author: Alan
 * @Date: 2018/12/1 10:38
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    /*****************************create*************************************/
    @RequestMapping(value = "/saveUser/{firstName}/{lastName}/{idCard}/{date}",method = RequestMethod.GET)
    @Transactional
    public User saveUser(@PathVariable String firstName,
                         @PathVariable String lastName,
                         @PathVariable String idCard,
                         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        User user = new User();//Id是自增长的，不需要传
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setIdCard(idCard);
        user.setDateOfBirth(date);
        return userService.save(user);
    }

    /*****************************Read***** ********************************/
    @RequestMapping(value = "/findByAndSort/{lastName}", method = RequestMethod.GET)
    public List<User> findByAndSort(@PathVariable String lastName) {
        Sort sort = new Sort(Sort.Direction.DESC, "firstName");
        return userService.findByAndSort(lastName, sort);
    }

    @RequestMapping(value = "/findByIdCard/{idCard}", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public User findByIdCard(@PathVariable(name = "idCard")  String idCard) {
        return userService.findByIdCard(idCard);
    }

    @RequestMapping(value = "/findByIdCard2/{idCard}", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public User findByIdCard2(@PathVariable(name = "idCard") String idCard) {
        return userService.findByIdCard2(idCard);
    }

    @RequestMapping(value = "/findByIdCard3/{idCard}", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public User findByIdCard3(@PathVariable(name = "idCard") String idCard) {
        return userService.findByIdCard3(idCard);
    }

    @RequestMapping(value = "/findByLastNameWithPageable/{lastName}", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public Page<User> findByLastNameWithPageable(@PathVariable String lastName) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(5,2,sort);
        return userService.findByLastNameWithPageable(lastName, pageable);
    }

    @RequestMapping(value = "/findByFirstNameOrLastName/{firstName}/{lastName}", method = RequestMethod.GET)
    public List<User> findByFirstNameOrLastName(@PathVariable String firstName,
                                                @PathVariable String lastName) {
        return userService.findByFirstNameOrLastName(firstName, lastName);
    }

    @RequestMapping(value = "/findByDateOfBirth/{date}", method = RequestMethod.GET)
    public List<User> findByDateOfBirth(@PathVariable(name = "date")
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return userService.findByDateOfBirth(date);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public List<User> findAll() {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        return userService.findAll(sort);
    }
    /*****************************update*************************************/
    @RequestMapping(value = "/updateUser/{firstName}/{idCard}", method = RequestMethod.GET)
    public int updateUser(@PathVariable String firstName, @PathVariable String idCard) {
        return userService.updateUser(firstName, idCard);
    }
    /*****************************delete*************************************/
    @RequestMapping(value = "/deleteByIdCard/{idCard}", method = RequestMethod.GET)
    @Transactional
    public String deleteByIdCard(@PathVariable String idCard) {
        userService.deleteByIdCard(idCard);
        return "SUCCESS";
    }

    @RequestMapping(value = "/deleteByIdCard2/{idCard}", method = RequestMethod.GET)
    @Transactional
    public String deleteByIdCard2(@PathVariable String idCard) {
        userService.deleteByIdCard2(idCard);
        return "SUCCESS";
    }

    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    @Transactional
    public String deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return "SUCCESS";
    }
}
