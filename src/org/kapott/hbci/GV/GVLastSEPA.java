/** 
 * Geschäftsvorfall SEPA Basislastschrift. Diese ist in pain.008.003.02.xsd spezifiziert.
 * @author Jan Thielemann
 */

package org.kapott.hbci.GV;

import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.LogFilter;
import org.kapott.hbci.sepa.PainVersion;
import org.kapott.hbci.sepa.PainVersion.Type;

/**
 * Implementierung des HBCI-Jobs fuer die SEPA-Basis-Lastschrift.
 */
public class GVLastSEPA extends AbstractSEPAGV
{
    private final static PainVersion DEFAULT = PainVersion.PAIN_008_001_01;
    
    /**
     * @see org.kapott.hbci.GV.AbstractSEPAGV#getDefaultPainVersion()
     */
    @Override
    protected PainVersion getDefaultPainVersion()
    {
        return DEFAULT;
    }

    /**
     * @see org.kapott.hbci.GV.AbstractSEPAGV#getPainType()
     */
    @Override
    protected Type getPainType()
    {
        return Type.PAIN_008;
    }

    /**
     * Liefert den Lowlevel-Jobnamen.
     * @return der Lowlevel-Jobname.
     */
    public static String getLowlevelName()
    {
        return "LastSEPA";
    }

    /**
     * ct.
     * @param handler
     */
    public GVLastSEPA(HBCIHandler handler)
    {
        super(handler, getLowlevelName());

    	// My bzw. src ist das Konto des Ausführenden. Dst ist das Konto des
    	// Belasteten.
    	addConstraint("src.bic",         "My.bic", null, LogFilter.FILTER_MOST);
    	addConstraint("src.iban",        "My.iban", null, LogFilter.FILTER_IDS);
    	
    	addConstraint("_sepadescriptor", "sepadescr", this.getPainVersion().getURN(), LogFilter.FILTER_NONE);
    	addConstraint("_sepapain",       "sepapain", null, LogFilter.FILTER_IDS);
    
    	addConstraint("src.bic",         "sepa.src.bic",  null,   LogFilter.FILTER_MOST);
    	addConstraint("src.iban",        "sepa.src.iban",  null,  LogFilter.FILTER_IDS);
    	addConstraint("src.name",        "sepa.src.name",  null,  LogFilter.FILTER_IDS);
    	addConstraint("dst.bic",         "sepa.dst.bic",   null,  LogFilter.FILTER_MOST);
    	addConstraint("dst.iban",        "sepa.dst.iban",  null,  LogFilter.FILTER_IDS);
    	addConstraint("dst.name",        "sepa.dst.name",  null,  LogFilter.FILTER_IDS);
    	addConstraint("btg.value",       "sepa.btg.value", null,  LogFilter.FILTER_NONE);
    	addConstraint("btg.curr",        "sepa.btg.curr",  "EUR", LogFilter.FILTER_NONE);
    	addConstraint("usage",           "sepa.usage",     null,  LogFilter.FILTER_NONE);
    
    	addConstraint("sepaid",          "sepa.sepaid",        getSEPAMessageId(),      LogFilter.FILTER_NONE);
    	addConstraint("endtoendid",      "sepa.endtoendid",    ENDTOEND_ID_NOTPROVIDED, LogFilter.FILTER_NONE);
    	addConstraint("mandateid",       "sepa.mandateid",     null,                    LogFilter.FILTER_NONE);
    	addConstraint("manddateofsig",   "sepa.manddateofsig", null,                    LogFilter.FILTER_NONE);
    	addConstraint("amendmandindic",  "sepa.amendmandindic",Boolean.toString(false), LogFilter.FILTER_NONE);
    }
}
