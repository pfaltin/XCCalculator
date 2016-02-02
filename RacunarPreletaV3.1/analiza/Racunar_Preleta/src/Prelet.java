import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("d010a22a-c40a-4aa9-9695-a1a30ee66369")
public class Prelet {
    @objid ("91c028a9-36c8-407f-9c21-2b69edf07ff0")
    public Date datumPreleta;

    @objid ("322fa163-419b-42f9-84b4-2b032d87feab")
    public Date vrijemeStarta;

    @objid ("4f7bf271-e8e1-4768-aebd-e46811ea8186")
    public Zrakoplov zrakoplov;

    @objid ("dd6e0358-1cba-4181-bb05-6bf6b1de6177")
    public Ruta ruta;

    @objid ("d6e22c20-c0df-40ce-bb5e-511058705866")
    public List<DionicaPreleta> dionicaPreleta = new ArrayList<DionicaPreleta> ();

    @objid ("77716e76-18d1-4f9a-8468-fa56f9c9b757")
    public prognozaProstorVrijeme prognozaProstorVrijemeGFS;

    @objid ("9fda578f-6fdc-471b-819f-bc366c96da07")
    public List<RutaPreletaIzracun> RutaPreletaIzracun = new ArrayList<RutaPreletaIzracun> ();

    @objid ("6aca2a3c-1df8-4550-9ad3-d2884ff6918f")
    public slojZraka slojZraka;

    @objid ("e862b9ab-36f0-4294-bc80-9cd2faaad163")
    public class GlavniProzor {
        @objid ("e5c60ab2-7c12-451d-90af-17247f5e5d1c")
        public PripremaGFSRuta pripremaGFSRuta;

        @objid ("0042f7de-bde6-4ac7-b6f3-aac3039cb811")
        public UpravljanjePodacima upravljanjePodacima;

        @objid ("bac977fb-1ae7-4522-80ef-a165003bf71d")
        public Prelet prelet;

        @objid ("1da630c7-c531-4fcf-9ebb-73d3da3e3a8f")
        public PrikazIzracuna prikazIzracuna;

        @objid ("67a6ea48-b0a7-464a-bec2-d376ee64bae4")
        public Zrakoplov zrakoplov;

        @objid ("adbc16ca-4916-4978-aff3-4dfa041fb548")
        public Ruta ruta;

        @objid ("601fcf06-5247-43ff-9834-b3c799661c80")
        public slojZraka slojZraka;

    }

}
