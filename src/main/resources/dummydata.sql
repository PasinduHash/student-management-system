INSERT INTO Student (id, register_name,full_name,dob,grade,address,contact,guardian_name,guardian_occupation,guardian_contact)
VALUES ('S001','Ruwan Kalpa','Loku Gamage Ruwan Kalpa Gamage', '1995-10-30','10-A', 'Yakkala','071-1258453','Priyantha Rajapaksha','Accountant','0716391216' ),
    ('S002','Ruwan Kalpa','Waththegedara Arachchilage Tharuka Devinda Waththegedara', '1995-10-30','10-A', 'Ekala','076-1258453','Manel Wanaguru','Teacher','07123591216' ),
    ('S003','Tharuka Devinda','Loku Gamage Ruwan Kalpa Gamage', '1995-10-30','10-B', 'Yakkala','071-1258453','Priyantha Rajapaksha','Accountant','0716391216' ),
    ('S004','Chethini Kavindya','Loku Gamage Ruwan Kalpa Gamage', '1995-10-30','10-A', 'Yakkala','071-1258453','Priyantha Rajapaksha','Accountant','0716391216' ),
    ('S005','Ruwan Kalpa','Loku Gamage Ruwan Kalpa Gamage', '1995-10-30','10-A', 'Yakkala','071-1258453','Priyantha Rajapaksha','Accountant','0716391216' )
;

INSERT INTO Book (id, name, author, category, status) VALUES
  ('B0001','Madol Duwa','Martin Wikrmasinha','NOVEL','BORROWED'),
  ('B0002','Gamperaliya','Martin Wikrmasinha','NOVEL','AVAILABLE'),
  ('B0003','Snakes','Tharuka Devinda','DOCUMENTS','BORROWED'),
  ('B0004','How to cook','Pasindu Hashen','EDUCATION','BORROWED'),
  ('B0005','Software Engineering I','Kasun Pradeep','EDUCATION','AVAILABLE'),
  ('B0006','Madol Duwa','Martin Wikrmasinha','NOVEL','AVAILABLE'),
  ('B0007','Madol Duwa','Martin Wikrmasinha','NOVEL','BORROWED')
;
INSERT INTO Library (book_id, student_id, date_borrowed, date_due, status, days_on_loan, days_overdue) VALUES
   ('B0001','S002','2022-04-10','2022-04-24','DUE',0,0),
   ('B0002','S001','2022-04-9','2022-04-24','DUE',0,0),
   ('B0003','S002','2022-04-8','2022-04-24','DUE',0,0),
   ('B0004','S003','2022-04-10','2022-04-24','DUE',0,0),
   ('B0001','S004','2022-04-12','2022-04-24','DUE',0,0),
   ('B0002','S005','2022-04-10','2022-04-24','DUE',0,0),
   ('B0005','S001','2022-04-12','2022-04-24','DUE',0,0),
   ('B0004','S002','2022-04-11','2022-04-24','DUE',0,0),
   ('B0006','S005','2022-04-15','2022-04-24','DUE',0,0),
   ('B0007','S002','2022-04-16','2022-04-24','DUE',0,0)
;