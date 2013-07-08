package com.surevine.alfresco.audit.repo;

import java.util.Date;

public class ResultCount {
    
    private Date _since;
    private int _count;
    
    public ResultCount(Date since, int count)
    {
        _since=since;
        _count=count;
    }
    
    public int getCount()
    {
        return _count;
    }
    
    public Date getSince()
    {
        // Use the copy constructor to not expose internal representation of a mutable object: see Effective Java.
        return new Date(_since.getTime());
    }

}
