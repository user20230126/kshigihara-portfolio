INSERT INTO daily_report_system.employees(code,name,role,password,delete_flg,created_at,updated_at)
     VALUES ("1","佐藤　太郎","ADMIN","$2a$10$vY93/U2cXCfEMBESYnDJUevcjJ208sXav23S.K8elE/J6Sxr4w5jO",0,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
INSERT INTO daily_report_system.employees(code,name,role,password,delete_flg,created_at,updated_at)
     VALUES ("2","田中　次郎","GENERAL","$2a$10$HPIjRCymeRZKEIq.71TDduiEotOlb8Ai6KQUHCs4lGNYlLhcKv4Wi",0,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
INSERT INTO daily_report_system.reports(report_date,title,content,employee_code,delete_flg,created_at,updated_at)
     VALUES (CURRENT_TIMESTAMP,"佐藤　太郎タイトル","佐藤　太郎アーティスト",1,0,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
INSERT INTO daily_report_system.reports(report_date,title,content,employee_code,delete_flg,created_at,updated_at)
     VALUES (CURRENT_TIMESTAMP,"田中　次郎タイトル","田中　次郎アーティスト",2,0,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
