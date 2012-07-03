package cn.com.liandisys.infa.service.account;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.liandisys.idev.ssi.service.impl.BaseServiceIbatisImpl;

/**
 * 安全相关实体的服务类,包括用户和权限组.
 * @param <T>
 * 
 * 
 */
// Spring Bean的标识.
@Service("accountService")
// 默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class AccountService<T> extends BaseServiceIbatisImpl<T> {

//    private static Logger logger = LoggerFactory
//            .getLogger(AccountService.class);
//
//    @Autowired
//    private UserDao userDao;
//    @Autowired
//    private GroupDao groupDao;
//    @Autowired
//    private ShiroDbRealm shiroRealm;
//
//    @Transactional(readOnly = false)
//    public void saveUser(User entity) {
//        userDao.save(entity);
//        shiroRealm.clearCachedAuthorizationInfo(entity.getLoginName());
//    }
//
//    /**
//     * 删除用户,如果尝试删除超级管理员将抛出异常.
//     */
//    @Transactional(readOnly = false)
//    public void deleteUser(Long id) {
//        if (isSupervisor(id)) {
//            logger.warn("操作员{}尝试删除超级管理员用户", SecurityUtils.getSubject()
//                    .getPrincipal());
//            throw new ServiceException("不能删除超级管理员用户");
//        }
//        userDao.delete(id);
//    }
//
//    /**
//     * 判断是否超级管理员.
//     */
//    private boolean isSupervisor(Long id) {
//        return id == 1;
//    }
//
//    public List<User> getAllUser() {
//        return (List<User>) userDao.findAll(new Sort(Direction.ASC, "id"));
//    }
//
//    public User findUserByLoginName(String loginName) {
//        return userDao.findByLoginName(loginName);
//    }
//
//    // -- Group Manager --//
//    public Group getGroup(Long id) {
//        return groupDao.findOne(id);
//    }
//
//    public List<Group> getAllGroup() {
//        return (List<Group>) groupDao.findAll((new Sort(Direction.ASC, "id")));
//    }
//
//    @Transactional(readOnly = false)
//    public void saveGroup(Group entity) {
//        groupDao.save(entity);
//        shiroRealm.clearAllCachedAuthorizationInfo();
//    }
//
//    @Transactional(readOnly = false)
//    public void deleteGroup(Long id) {
//        groupDao.delete(id);
//        shiroRealm.clearAllCachedAuthorizationInfo();
//    }
}
