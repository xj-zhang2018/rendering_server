package org.xjtu.framework.core.base.constant;

public class UnitStatus {
	public static final int unavailable=-1; //单元初始化后可能收不到master节点的ip信息，此时单元不可用
	//public static final int free=0;
	public static final int busy=1;
	public static final int dead=2;
	public static final int available=3;
}
