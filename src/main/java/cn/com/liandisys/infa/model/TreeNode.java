package cn.com.liandisys.infa.model;

public class TreeNode {
	private String id;
	private String parent;
	private String text;
	private boolean leaf;
	private Object checked;
	private String state;
	private String iconCls;
	private boolean disabled;
	private int print;
	private Attributes attributes;

	public Attributes getAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	public TreeNode(String id, String text, String parent) {
		this.id = id;
		this.text = text;
		this.parent = parent;
		this.print = 0;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getParent() {
		return this.parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public int getPrint() {
		return this.print;
	}

	public void setPrint(int print) {
		this.print = print;
	}

	public boolean isLeaf() {
		return this.leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public String getIconCls() {
		return this.iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public Object getChecked() {
		return this.checked;
	}

	public void setChecked(Object checked) {
		this.checked = checked;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isDisabled() {
		return this.disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}