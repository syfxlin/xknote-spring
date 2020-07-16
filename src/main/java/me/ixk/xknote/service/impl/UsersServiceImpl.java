package me.ixk.xknote.service.impl;

import me.ixk.xknote.entity.Users;
import me.ixk.xknote.mapper.UsersMapper;
import me.ixk.xknote.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author syfxlin
 * @since 2020-07-01
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

}
