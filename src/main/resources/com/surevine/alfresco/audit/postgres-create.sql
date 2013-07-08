CREATE TABLE alf_accounting_audit
(
    id SERIAL NOT NULL, --Unique ID not guaranteed to be contigious.  It is assumed that selection from this table is constrained by this column
    tstamp TIMESTAMP NOT NULL, --Time the audit event was created - local to the backend box
    username varchar(40),-- NOT NULL, --Logon name of the user performing the event
    action varchar(40) NOT NULL, --The type of event performed by the user
    source varchar(80), --Identifier of the item the event is about
    secLabel varchar(1024), --Security label of the item the event is about after the event was performed
    details varchar(256), --Action specific details of the action
    success varchar(10), --Did the action succeed? "true" or "false"
    site varchar(80), --Id of the site whose data was manipulated by the action
    url varchar(256), --URL that can be visited to 'see' the item manipulated by the action in it's current form
    version varchar(10), --Verion number of the item manipulated after it was manipulated.  NULL => 1.0.0
    remote_addr varchar(40), --Remote address of the client invoking the action.  Note this will usually just be a proxy server
    tags varchar(256), --Tags placed upon the item manipulated by the action after the action
    node_ref varchar(80), --NodeRef of the item manipulated by the action
    time_spent NUMERIC(19,0), -- Time (in ms) spent servicing the action
    PRIMARY KEY (id)
);

GRANT SELECT, INSERT ON alf_accounting_audit TO alfresco;
GRANT ALL ON alf_accounting_audit_id_seq TO alfresco;