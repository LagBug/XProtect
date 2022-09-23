package me.lagbug.xprotect.global;

import java.util.Arrays;
import java.util.List;

/**
* Class used to store all the permissions
* used by this plugin.
*
* @version 1.0
*/
public class Permissions {

	public static final String ALL = "xprotect.*";
	public static final String USE = "xprotect.use";
	public static final String BYPASS = "xprotect.bypass";
	public static final String NOTIFY = "xprotect.notify";
	public static final String UPDATE = "xprotect.update";
	
	public static final String ADD = "xprotect.add";
	public static final String REMOVE = "xprotect.remove";
	public static final String CHECK = "xprotect.check";
	public static final String RELOAD = "xprotect.reload";
	public static final String TEST = "xprotect.test";
	public static final String DEBUG = "xprotect.debug";
	public static final String LIST = "xprotect.list";
	public static final String BLACKLIST = "xprotect.blacklist";
	
	//All the permissions given as a List
	public static final List<String> ALL_PERMISSIONS = Arrays.asList(ALL, BLACKLIST, USE, BYPASS, NOTIFY, ADD, REMOVE, CHECK, RELOAD, TEST, DEBUG, UPDATE, LIST);
	
}