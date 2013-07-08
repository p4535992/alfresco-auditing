drop table if exists ALF_ACCOUNTING_AUDIT;

create table ALF_ACCOUNTING_AUDIT (
	id INTEGER auto_increment primary key,
	tstamp TIMESTAMP,
	username varchar(40),
	action varchar(40),
	source varchar(80),
	secLabel varchar(256),
	details varchar(256),
	success varchar(10),
	site varchar(80),
	url varchar(256),
	version varchar(10),
	remote_addr varchar(40),
	tags varchar(256),
    node_ref varchar(80),
    time_spent BIGINT
);

