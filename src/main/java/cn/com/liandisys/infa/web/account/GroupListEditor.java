package cn.com.liandisys.infa.web.account;

import java.beans.PropertyEditorSupport;

import org.springframework.stereotype.Component;

/**
 * 用于转换用户表单中复杂对象Group的checkbox的关联。
 * 
 * 
 */
@Component
public class GroupListEditor extends PropertyEditorSupport {
//	private AccountService accountService;

//	@Override
//	public void setAsText(String text) throws IllegalArgumentException {
//		String[] ids = StringUtils.split(text, ",");
//		List<Group> groups = new ArrayList<Group>();
//		for (String id : ids) {
//			Group group = accountService.getGroup(Long.valueOf(id));
//			groups.add(group);
//		}
//		setValue(groups);
//	}
//
//	@Override
//	public String getAsText() {
//		return Collections3.extractToString((List) getValue(), "id", ",");
//	}

//	@Autowired
//	public void setAccountManager(AccountService accountService) {
//		this.accountService = accountService;
//	}

}
