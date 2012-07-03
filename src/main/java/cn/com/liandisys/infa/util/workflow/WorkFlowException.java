package cn.com.liandisys.infa.util.workflow;

/**
 * Informatica异常类
 * 
 * @author 
 * 
 */


public class WorkFlowException extends Exception {

	private static final long serialVersionUID = 1L;

	public static final int SERVER_SHUTTING_DOWN = -200;

	public static final int REPSERVER_UNREACHABLE = -201;

	public static final int LOGIN_REQUIRED = -202;

	public static final int ALREADY_LOGGED_IN = -203;

	public static final int MISMATCHED_REPOSITORY = -204;

	public static final int INVALID_REPOSITORY_USER_OR_PASSWORD = -205;

	public static final int INSUFFICIENT_USER_PRIVILEGE = -206;

	public static final int INVALID_SHUTDOWN_MODE = -207;

	public static final int FOLDER_NOT_EXIST = -209;

	public static final int WORKFLOW_NOT_EXIST = -210;

	public static final int WORKFLOW_NOT_UNIQUE = -211;

	public static final int TASK_NOT_EXIST = -212;

	public static final int TASK_NOT_UNIQUE = -213;

	public static final int INVALID_FOLDER_ID = -214;

	public static final int INVALID_WORKFLOW_ID = -215;

	public static final int INVALID_TASK_ID = -216;

	public static final int NOT_EXPECTING_KEYS_OR_ATTRIBUTES = -218;

	public static final int WORKFLOW_DISABLED = -219;

	public static final int WORKFLOW_ALREADY_RUNNING = -220;

	public static final int WORKFLOW_NOT_RUNNING = -221;

	public static final int CANNOT_ACQUIRE_EXECUTION_LOCK = -224;

	public static final int TASK_NOT_RUNNING = -225;

	public static final int RESULTS_NOT_AVAILABLE = -226;

	public static final int INVALID_MONITOR_SERVER_MODE = -227;

	public static final int CANNOT_FREE_MEMORY = -228;

	public static final int UNEXPECTED_ERROR = -229;

	public static final int SCHED_ELAPSED = -230;

	public static final int RUNONDEMAND_NOT_SCHED = -231;

	public static final int SCHED_FAILED_NO_REPO_CONN = -232;

	public static final int SCHED_WORKFLOW_FETCH_FAILED = -233;

	public static final int SCHED_NOT_VALID = -234;

	public static final int SCHED_NOT_ENABLED = -235;

	public static final int SCHED_FAILED_WRONG_SERVER = -236;

	public static final int SCHED_BAD_TIMER = -237;

	public static final int MONITOR_JOB_NOT_FOUND = -238;

	public static final int SESSION_RUN_NOT_FOUND = -239;

	public static final int SESSION_PERF_DATA_NOT_COLLECTED = -240;

	public static final int EXPECTING_KEYS_OR_ATTRIBUTES = -241;

	public static final int DUPLICATE_ATTRIBUTE = -242;

	public static final int DUPLICATE_KEY = -243;

	public static final int CANT_STOP_OR_ABORT_TASK_TYPE = -244;

	public static final int TASK_NOT_SUSPEND = -245;

	public static final int WORKFLOW_NOT_SUSPEND = -246;

	public static final int LOG_FILE_NOT_FOUND = -247;

	public static final int LAST_WORKFLOW_RUN_NOT_FOUND = -248;

	public static final int TASK_NOT_RUN = -249;

	public static final int READ_LOG_FILE_FAILED = -250;

	public static final int SESSION_LOG_FILE_UNAVAILABLE = -251;

	public static final int WORKFLOW_START_FAILED = -252;

	public static final int WORKFLOW_EXEC_DISABLED = -253;

	public static final int WORKFLOW_EXEC_FAILED = -254;

	public static final int WORKFLOW_EXEC_STOPPED = -255;

	public static final int WORKFLOW_EXEC_ABORTED = -256;

	public static final int TASK_START_FAILED = -257;

	public static final int TASK_EXEC_DISABLED = -258;

	public static final int TASK_EXEC_FAILED = -259;

	public static final int TASK_EXEC_STOPPED = -260;

	public static final int TASK_EXEC_ABORTED = -261;

	public static final int PARAM_FILE_NOT_FOUND = -262;

	public static final int PARAM_MISSING_OR_INVALID = -263;

	public static final int WORKFLOW_RUN_MISMATCH = -264;

	public static final int TASK_RUN_MISMATCH = -265;

	public static final int TASK_INSTANCE_NOT_FOUND = -266;

	public static final int SCHEDULE_NONE = -267;

	public static final int CANNOT_FIND_WORKFLOW_LOG_NAME = -268;

	public static final int WORKFLOW_FETCH_FAILED = -269;

	public static final int SERVER_CANNOT_DEBUG_MAPPING = -270;

	public static final int DATABASE_ERROR = -271;

	public static final int REQUEST_OBJECT_MISSING = -272;

	public static final int REQUEST_OBJECT_MISMATCH = -273;

	public static final int SESSION_LOG_FILE_NOT_CREATED = -274;

	public static final int SCHED_OUT_OF_RANGE = -275;

	public static final int NOT_INITIALIZED = -276;

	public static final int ALREADY_INITIALIZED = -277;

	public static final int ALREADY_DEINITIALIZED = -278;

	public static final int RESOURCE_IN_USE = -279;

	public static final int REQUEST_TYPE_NOT_SUPPORTED = -280;

	public static final int FAILURE = -1;
	public static final int INCOMPATIBLE_SERVER = -2;
	public static final int INCOMPATIBLE_INTERFACE_TYPE = -3;
	public static final int INCOMPATIBLE_INTERFACE_VERSION = -4;
	public static final int UNKNOWN_REQUEST_TYPE = -5;
	public static final int REQUEST_NOT_FOUND = -6;
	public static final int REQUEST_NOT_STOPPABLE = -7;
	public static final int TOO_MANY_REPLIES = -8;
	public static final int INTERNAL_ERROR = -101;
	public static final int NO_NOTIFICATION_HANDLER = -102;
	public static final int NOTIFICATION_HANDLER_ALREADY_REGISTERED = -103;
	public static final int NO_CALLBACK_FUNCTION = -104;
	public static final int OUT_OF_MEMORY = -105;
	public static final int INVALID_SERVER_ADDESS = -106;
	public static final int SERVER_TIMEOUT = -107;
	public static final int HOST_UNREACHABLE = -108;
	public static final int CANNOT_ESTABLISH_CONNECTION = -109;
	public static final int LOST_CONNECTION = -110;
	public static final int INVALID_REPLY_CONTEXT = -111;
	public static final int INVALID_NOTIFICATION = -112;
	public static final int NULL_PARAMETER_NOT_ALLOWED = -113;
	public static final int INVALID_CONNECTION_ID = -114;
	public static final int ALREADY_CONNECTED = -115;
	public static final int NOT_CONNECTED = -116;
	public static final int CANNOT_CREATE_LOCALE = -117;
	public static final int OPERATION_NOT_ALLOWED = -118;
	public static final int INVALID_TIMEOUT = -119;
	public static final int INVALID_CODEPAGE_ID = -120;
	public static final int CREATE_LOCALE_FAILED = -121;
	public static final int INVALID_LOCALE = -122;
	public static final int CONVERSION_FAILED = -123;
	public static final int INVALID_DATE = -124;
	public static final int CONNECTION_ABORTED = -125;
	public static final int LOCALE_ALREADY_SET = -126;
	public static final int LOCALE_NOT_SET = -127;
	public static final int REQUEST_DISABLED_IN_CALLBACK = -128;
	public static final int DUPLICATE_NOTIFICATION_SUBSCRIPTION = -129;
	public static final int SYSTEM_ERROR = -300;

	private int errCode;

	public WorkFlowException() {
		super();
	}

	public WorkFlowException(int errCode) {
		super();
		this.errCode = errCode;
	}

	@Override
	public String getMessage() {
		switch (errCode) {
		case CANNOT_ESTABLISH_CONNECTION:
			return "无法建立连接";
		case WORKFLOW_NOT_EXIST:
			return "WorkFlow不存在";
		case WORKFLOW_ALREADY_RUNNING:
			return "此WorkFlow已在运行";
		case MISMATCHED_REPOSITORY:
			return "无效存储库";
		case INVALID_REPOSITORY_USER_OR_PASSWORD:
			return "登录用户名或密码有误";
		case ALREADY_INITIALIZED:
			return "初始化已完成";
		case CANNOT_CREATE_LOCALE:
			return "无法创建Locale";
		case CANNOT_FREE_MEMORY:
			return "无法释放内存";
		case CONNECTION_ABORTED:
			return "连接中止";
		case CONVERSION_FAILED:
			return "转换失败";
		case CREATE_LOCALE_FAILED:
			return "创建LOCALE失败";
		case DATABASE_ERROR:
			return "数据库异常";
		case FAILURE:
			return "失败";
		case FOLDER_NOT_EXIST:
			return "文件夹不存在";
		case INVALID_CONNECTION_ID:
			return "无效的连接ID";
		case INVALID_LOCALE:
			return "无效的LOCALE";
		case INVALID_WORKFLOW_ID:
			return "无效的WORKFLOW id";
		case NOT_INITIALIZED:
			return "未初始化";
		case OUT_OF_MEMORY:
			return "内存溢出";
		case TASK_NOT_EXIST:
			return "Task不存在";
		case WORKFLOW_NOT_RUNNING:
			return "WorkFlow未运行";
		case WORKFLOW_NOT_SUSPEND:
			return "WorkFlow未暂停";
		case WORKFLOW_START_FAILED:
			return "WorkFlow启动失败";
		case SYSTEM_ERROR:
			return "系统错误";
		default:
			return "未定义异常";
		}
	}

	public int getErrCode() {
		return errCode;
	}
}