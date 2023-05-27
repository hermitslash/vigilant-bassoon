INSERT INTO "general".app_roles (role_id,role_name) VALUES
	 (1,'ROLE_ADMIN'),
	 (2,'ROLE_USER'),
	 (3,'SCOPE_RS_WRITE'),
	 (4,'SCOPE_RS_READ'),
	 (5,'SCOPE_RS_UPDATE'),
	 (6,'SCOPE_RS_DELETE'),
	 (7,'SCOPE_TOKEN_READ'),
	 (8,'SCOPE_DOWNLOAD_REPORT'),
	 (9,'SCOPE_RS_EOD_READ');
INSERT INTO "general".app_users (user_id,account_non_expired,account_non_locked,company_name,credentials_non_expired,date_created,email_address,enabled,first_name,last_name,last_updated,"password",phone_number,username) VALUES
	 (1,true,true,'uksmg',true,'2023-05-17 01:54:00.916795+05:30','ukadsmg@gmail.com',true,'Udaya','Kumar','2023-05-17 01:54:00.917273+05:30','$2a$10$hKTch3q5gaqQVHlbFtpwEevB72PRvHnzUzv6GDi2Y5N/.5u/AI1Fy','9448638133','ukadsmg'),
	 (2,true,true,'uksmg',true,'2023-05-17 01:54:01.459514+05:30','testuser1@gmail.com',true,'test1','user1','2023-05-17 01:54:01.459577+05:30','$2a$10$dCd0dH/wKjC8kRW4GvtVm.e.SlLoHQ5QiFgqffn5GLq/G8Q/nvPLy','9448638133','testuser1');
INSERT INTO "general".user_roles (user_id,role_id) VALUES
	 (1,2),
	 (1,6),
	 (1,4),
	 (1,8),
	 (1,5),
	 (1,1),
	 (1,3),
	 (1,7),
	 (1,9),
	 (2,2);
INSERT INTO "general".user_roles (user_id,role_id) VALUES
	 (2,4),
	 (2,8);