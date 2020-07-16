package me.ixk.xknote.service.impl;

import me.ixk.xknote.entity.GitInfo;
import me.ixk.xknote.mapper.GitInfoMapper;
import me.ixk.xknote.service.IGitInfoService;
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
public class GitInfoServiceImpl extends ServiceImpl<GitInfoMapper, GitInfo> implements IGitInfoService {

}
