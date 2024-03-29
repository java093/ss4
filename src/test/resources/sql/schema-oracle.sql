﻿DROP TABLE IF EXISTS  T_SYS_ORG;
DROP TABLE IF EXISTS  T_SYS_SYSTEM;
DROP TABLE IF EXISTS  T_SYS_ROLE;
DROP TABLE IF EXISTS  T_SYS_USER;
DROP TABLE IF EXISTS  T_SYS_USER_ROLE;
DROP TABLE IF EXISTS  T_SYS_FUNC;
DROP TABLE IF EXISTS  T_SYS_OBJECT;

DROP TABLE IF EXISTS  T_SYS_ROLE_FUNC;
DROP TABLE IF EXISTS  T_SYS_ROLE_OBJECT;

DROP TABLE IF EXISTS  T_SYS_USER_FUNC;
DROP TABLE IF EXISTS  T_SYS_USER_OBJECT;

DROP SEQUENCE IF EXISTS SYS_SEQUENCE;

CREATE TABLE T_SYS_ORG
(
   ID         			NUMBER(19)				NOT NULL				,
   NAME       			VARCHAR2(100)		NOT NULL				,
   PARENT_ID   		NUMBER(19)             				DEFAULT 0	,
   CODE                 VARCHAR(100)		NOT NULL				,
   SHORT_NAME           VARCHAR(50)									,
   AVALIABLE         	CHAR(1)           	NOT NULL  	DEFAULT 'Y'	,
   MEMO                 VARCHAR(250)								,
   PRIMARY KEY (ID)
);

COMMENT ON TABLE T_SYS_ORG IS '组织结构表';
COMMENT ON COLUMN T_SYS_ORG.AVALIABLE IS 'Y:是,N:否';
COMMENT ON COLUMN T_SYS_ORG.PARENT_ID IS '上级组织ID,根节点ID为0';
COMMENT ON COLUMN T_SYS_ORG.SHORT_NAME IS '组织简称';
COMMENT ON COLUMN T_SYS_ORG.CODE IS '组织编码CODE，包括组织层次的含义,每层次固定4位数字构成的Code值，例如"000100010001...."';

CREATE TABLE T_SYS_SYSTEM
(
   ID                   NUMBER(19)              NOT NULL				,
   NAME			        VARCHAR(100)		NOT NULL				,
   BUSI_CODE            VARCHAR(100)		NOT NULL				,
   DEPLOY_URL           VARCHAR(250)								,
   AVALIABLE            CHAR(1)				NOT NULL 	DEFAULT 'Y'	,
   PRIMARY KEY (ID)
);

COMMENT ON TABLE T_SYS_SYSTEM IS '业务系统表';
COMMENT ON COLUMN T_SYS_SYSTEM.AVALIABLE IS 'Y:是,N:否';
COMMENT ON COLUMN T_SYS_SYSTEM.DEPLOY_URL IS '业务系统URL';

CREATE TABLE T_SYS_ROLE(
    ID 					NUMBER(19) 				NOT NULL 				,
    NAME				VARCHAR(100)		NOT NULL				,
    CODE 				VARCHAR(100)		NOT NULL				,
    SYSTEM_ID 			NUMBER(19)				NOT NULL				,
    ORG_ID 				NUMBER(19)										,
    DESCRIPTION 		VARCHAR(250)								,
    AVALIABLE            CHAR(1)			NOT NULL 	DEFAULT 'Y'	,
    PRIMARY KEY (ID),
    FOREIGN KEY(ORG_ID) 	REFERENCES T_SYS_ORG(ID),
    FOREIGN KEY(SYSTEM_ID)	REFERENCES T_SYS_SYSTEM (ID)
);

COMMENT ON TABLE T_SYS_ROLE IS '角色表';

CREATE TABLE T_SYS_USER
(
   ID		            NUMBER(19)            	NOT NULL				,
   NAME		            VARCHAR(100)		NOT NULL				,
   ORG_ID         		NUMBER(19)										,
   AVALIABLE          	CHAR(1)             NOT NULL  	DEFAULT 'Y'	,
   ADMIN          		CHAR(1)             NOT NULL  	DEFAULT 'N'	,
   LOGIN_NAME           VARCHAR(100)								,
   PASSWORD             VARCHAR(100)								,
   PRIMARY KEY (ID),
   FOREIGN KEY(ORG_ID) REFERENCES T_SYS_ORG (ID)
);

COMMENT ON TABLE T_SYS_USER IS '人员表';
COMMENT ON COLUMN T_SYS_USER.LOGIN_NAME IS '登录帐号';
COMMENT ON COLUMN T_SYS_USER.AVALIABLE IS 'Y:是,N:否';
COMMENT ON COLUMN T_SYS_USER.ADMIN IS 'Y:系统管理员,N:非系统管理员';

CREATE TABLE T_SYS_USER_ROLE
(
   USER_ID              NUMBER(19)              NOT NULL				,
   ROLE_ID              NUMBER(19)              NOT NULL				,
   PRIMARY KEY (USER_ID, ROLE_ID),
   FOREIGN KEY(ROLE_ID) REFERENCES T_SYS_ROLE(ID),
   FOREIGN KEY(USER_ID) REFERENCES T_SYS_USER (ID)
);

COMMENT ON TABLE T_SYS_USER_ROLE IS '用户角色对应表';

CREATE TABLE T_SYS_FUNC
(
   ID                   NUMBER(19)              NOT NULL				,
   NAME            		VARCHAR(100)		NOT NULL				,
   SYSTEM_ID            NUMBER(19)				NOT NULL				,
   CODE		            VARCHAR(100)		NOT NULL				,
   BUSI_CODE            VARCHAR(100)								,
   FUNC_URL	            VARCHAR(250)								,
   HELP_URL             VARCHAR(250)								,
   LEAF             	CHAR(1)				NOT NULL				,
   PARENT_ID            NUMBER(19)				NOT NULL	DEFAULT 0	,
   FUNC_TYPE            CHAR(1)										,
   AVALIABLE          	CHAR(1)             NOT NULL  	DEFAULT 'Y'	,
   FUNC_ICON            VARCHAR(100)								,	
   PRIMARY KEY (ID),
   FOREIGN KEY(SYSTEM_ID) REFERENCES T_SYS_SYSTEM(ID),
);

COMMENT ON TABLE T_SYS_FUNC IS '菜单表';
COMMENT ON COLUMN T_SYS_FUNC.LEAF IS 'Y:是,N:否';
COMMENT ON COLUMN T_SYS_FUNC.FUNC_TYPE IS 'F-自由功能 A-授权使用';
COMMENT ON COLUMN T_SYS_FUNC.AVALIABLE IS 'Y:是,N:否';
COMMENT ON COLUMN T_SYS_FUNC.PARENT_ID IS '上级功能菜单，根节点ID为0';
COMMENT ON COLUMN T_SYS_FUNC.FUNC_URL IS '功能URL';
COMMENT ON COLUMN T_SYS_FUNC.CODE IS '组织编码CODE，包括组织层次的含义,每层次固定4位数字构成的Code值，例如"000100010001...."';
COMMENT ON COLUMN T_SYS_FUNC.BUSI_CODE IS '业务相关的编码CODE';

CREATE TABLE T_SYS_OBJECT
(
   ID                   NUMBER(19)              NOT NULL				,
   NAME         		VARCHAR(100)		NOT NULL				,
   BUSI_CODE            VARCHAR(100)								,
   FUNC_ID              NUMBER(19)				NOT NULL				,
   PRIMARY KEY (ID),
   FOREIGN KEY(FUNC_ID) REFERENCES T_SYS_FUNC(ID)
);

COMMENT ON TABLE T_SYS_OBJECT IS '菜单按钮对象表';

CREATE TABLE T_SYS_ROLE_FUNC
(
   ROLE_ID              NUMBER(19)              NOT NULL				,
   FUNC_ID	            NUMBER(19)              NOT NULL				,
   PRIMARY KEY (ROLE_ID, FUNC_ID),
   FOREIGN KEY(ROLE_ID) REFERENCES T_SYS_ROLE(ID),
   FOREIGN KEY(FUNC_ID) REFERENCES T_SYS_FUNC(ID)
);

COMMENT ON TABLE T_SYS_ROLE_FUNC IS '按角色授权时，角色-菜单功能 关系表';

CREATE TABLE T_SYS_ROLE_OBJECT
(
   ROLE_ID              NUMBER(19)              NOT NULL				,
   FUNC_ID				NUMBER(19)              NOT NULL				,
   OBJECT_ID            NUMBER(19)              NOT NULL				,
   PRIMARY KEY (ROLE_ID, FUNC_ID, OBJECT_ID),
   FOREIGN KEY(FUNC_ID) REFERENCES T_SYS_FUNC(ID),
   FOREIGN KEY(ROLE_ID) REFERENCES T_SYS_ROLE(ID)
);

COMMENT ON TABLE T_SYS_ROLE_OBJECT IS '按角色授权时，角色-菜单按钮对象 关系表';

CREATE TABLE T_SYS_USER_FUNC
(
   USER_ID              NUMBER(19)         		NOT NULL				,
   FUNC_ID              NUMBER(19)              NOT NULL				,
   PRIMARY KEY (USER_ID, FUNC_ID),
   FOREIGN KEY(FUNC_ID) REFERENCES T_SYS_FUNC(ID),
   FOREIGN KEY(USER_ID) REFERENCES T_SYS_USER(ID)
);

COMMENT ON TABLE T_SYS_USER_FUNC IS '按用户授权时，用户-菜单功能 关系表';

CREATE TABLE T_SYS_USER_OBJECT
(
	USER_ID				NUMBER(19)              NOT NULL				,
	FUNC_ID				NUMBER(19)              NOT NULL				,
	OBJECT_ID			NUMBER(19)              NOT NULL				,
	PRIMARY KEY (USER_ID,FUNC_ID,OBJECT_ID),
    FOREIGN KEY(USER_ID) REFERENCES T_SYS_USER(ID),
    FOREIGN KEY(FUNC_ID) REFERENCES T_SYS_FUNC(ID)
);

COMMENT ON TABLE T_SYS_USER_OBJECT IS '按用户授权时，用户-菜单功能-菜单按钮对象 关系表';

CREATE SEQUENCE SYS_SEQUENCE;
