package cn.com.liandisys.infa.model;

public class Attributes {
	
	/**
	 * 是否叶子
	 */
	private boolean leaf;
	/**
	 * 类型
	 */
	private String state;
	/**
     * 集团名称
     */
    private String groupName;
    /**
     * 电厂名称
     */
    private String powerStationName;
    /**
     * 集团ID
     */
    private Long groupId;
    /**
     * 电厂ID
     */
    private Long powerStationId;
    /**
     * 容量
     */
    private String capacity;
    
    /**
     *url路径 
     */
    private String menuurl;
    
    /**
     * 菜单图标
     */
    private String iconname;
    
	
	public String getMenuurl() {
		return menuurl;
	}
	public void setMenuurl(String menuurl) {
		this.menuurl = menuurl;
	}
	public String getIconname() {
		return iconname;
	}
	public void setIconname(String iconname) {
		this.iconname = iconname;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getPowerStationName() {
		return powerStationName;
	}
	public void setPowerStationName(String powerStationName) {
		this.powerStationName = powerStationName;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Long getPowerStationId() {
		return powerStationId;
	}
	public void setPowerStationId(Long powerStationId) {
		this.powerStationId = powerStationId;
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public Attributes(String state,boolean leaf){
		this.state = state;
		this.leaf = leaf;
	}

}
