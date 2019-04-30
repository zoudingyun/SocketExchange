package per.zdy.socketexchangeclientcp.service.Impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import per.zdy.socketexchangeclientcp.domain.Pojo.User;
import per.zdy.socketexchangeclientcp.service.UserRepository;
import per.zdy.socketexchangeclientcp.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /**************************create********************************/
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
    /**************************read********************************/
    @Override
    public List<User> findByAndSort(String lastName, Sort sort) {
        return userRepository.findByAndSort(lastName, sort);
    }

    @Override
    public User findByIdCard(String idCard) {
        return userRepository.findByIdCard(idCard);
    }

    @Override
    public User findByIdCard2(String idCard) {
        return userRepository.findByIdCard2(idCard);
    }

    @Override
    public User findByIdCard3(String idCard) {
        return userRepository.findByIdCard3(idCard);
    }

    @Override
    public Page<User> findByLastNameWithPageable(String lastName, Pageable pageable) {
        return userRepository.findByLastNameWithPageable(lastName, pageable);
    }

    @Override
    public List<User> findByFirstNameOrLastName(String firstName, String lastName) {
        return userRepository.findByFirstNameOrLastName(firstName, lastName);
    }

    @Override
    public List<User> findByDateOfBirth(@Param("date") LocalDate date) {
        return userRepository.findByDateOfBirth(date);
    }

    @Override
    public List<User> findAll(Sort sort) {
        return userRepository.findAll(sort);
    }

    /**************************update********************************/
    @Override
    public int updateUser(String firstName, String idCard) {
        return userRepository.updateUser(firstName, idCard);
    }
    /**************************delete********************************/
    @Override
    public void deleteByIdCard(String idCard) {
        userRepository.deleteByIdCard(idCard);
    }

    @Override
    public void deleteByIdCard2(String idCard) {
        userRepository.deleteByIdCard2(idCard);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
