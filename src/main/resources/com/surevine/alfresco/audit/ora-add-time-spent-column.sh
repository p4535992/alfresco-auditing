#!/bin/bash

ALFRESCO_DB_USERNAME=alfresco
ALFRESCO_DB_PASSWORD=orcl_alfdb_pw

ORACLE_HOME=/usr/lib/oracle/xe/app/oracle/product/10.2.0/server
SQLPLUS=$ORACLE_HOME/bin/sqlplus

$SQLPLUS $ALFRESCO_DB_USERNAME/$ALFRESCO_DB_PASSWORD <<EOF || exit 1
alter table alf_accounting_audit add time_spent NUMBER(19,0);
quit
EOF