package me.ixk.xknote.service.impl;

import me.ixk.xknote.entity.UserConfig;
import me.ixk.xknote.mapper.UserConfigMapper;
import me.ixk.xknote.service.IUserConfigService;
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
public class UserConfigServiceImpl extends ServiceImpl<UserConfigMapper, UserConfig> implements IUserConfigService {

}
