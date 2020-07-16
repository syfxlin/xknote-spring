package me.ixk.xknote.service.impl;

import me.ixk.xknote.entity.Config;
import me.ixk.xknote.mapper.ConfigMapper;
import me.ixk.xknote.service.IConfigService;
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
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements IConfigService {

}
