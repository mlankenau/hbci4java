
/*  $Id: AbstractMultiGV.java,v 1.2 2011/05/17 16:39:07 willuhn Exp $

    This file is part of HBCI4Java
    Copyright (C) 2001-2008  Stefan Palme

    HBCI4Java is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    HBCI4Java is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package org.kapott.hbci.GV;

import java.util.Iterator;
import java.util.List;

import org.kapott.hbci.GV_Result.HBCIJobResultImpl;
import org.kapott.hbci.datatypes.SyntaxWrt;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.swift.DTAUS;

public abstract class AbstractMultiGV 
  extends HBCIJobImpl 
{
    public AbstractMultiGV(HBCIHandler handler, String jobnameLL, HBCIJobResultImpl jobResult) 
    {
        super(handler, jobnameLL, jobResult);
    }

    public String getChallengeParam(String path)
    {
        String ret=null;
        
        if (path.startsWith("sum")) {
            String dtausdata=getLowlevelParams().getProperty(getName()+".data");
            DTAUS  dtaus=new DTAUS(dtausdata.substring(1));
            
            if (path.equals("sumOthers")) {
            	// summe der ziel-kontonummern
            	long sum=0;
            	List entries=dtaus.getEntries();
            	for (Iterator i=entries.iterator();i.hasNext();) {
            		DTAUS.Transaction entry=(DTAUS.Transaction)i.next();
            		sum += Long.parseLong(entry.otherAccount.number);
            	}
            	ret=Long.toString(sum);
            	if (ret.length()>10) {
            		ret=ret.substring(0,10);
            	}
            	
            } else if (path.equals("sumValue")) {
                // summe der betr�ge
                long sum = 0;
                List entries = dtaus.getEntries();
                for (Iterator i = entries.iterator(); i.hasNext();) {
                    DTAUS.Transaction entry = (DTAUS.Transaction) i.next();
                    sum += entry.value.getLongValue();
                }
                // TODO: hier wird ziemlich unsch�n direkt auf
                // die SyntaxDE-Funktionen zugegriffen
                String v = HBCIUtils.value2String(sum / 100.0);
                ret = new SyntaxWrt(v, 1, 0).toString();
            	
            } else if (path.equals("sumCurr")) {
            	// w�hrung des sammlers
                ret=dtaus.getCurr()==DTAUS.CURR_DM?"DEM":"EUR";
            
            } else if (path.equals("sumCount")) {
              // willuhn 2011-05-17 Anzahl der Buchungen, HHD 1.4
              // Anzahl der Buchungen
              ret = Integer.toString(dtaus.getEntries().size());
            }
        } else {
            ret=super.getChallengeParam(path);
        }
        
        return ret;
    }
}
