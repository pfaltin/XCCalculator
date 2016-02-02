import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("5c151b2d-405d-46ca-a1db-467ee5ed595a")
public class prognozaProstorVrijeme {
    @objid ("a0788e9b-3818-45eb-b233-e56633bc0a69")
    public float dizanjeBrzina;

    @objid ("28f055bf-fe27-4e8d-9d21-2608f6b8874e")
    public int dizanjeVisina;

    @objid ("60f06663-bc77-4bfd-8ed3-afcb441a164b")
    public int vjetarSmjer;

    @objid ("7e99e881-f4b9-40a8-af87-d661665a1144")
    public float vjetarBrzina;

    @objid ("75960999-cce9-420e-97e5-3b2921d6484e")
    public List<TockaMeteo> tockaGFS = new ArrayList<TockaMeteo> ();

    @objid ("ee87e508-b07f-43d7-a718-b2647829e316")
    public Tocka tocka;

}
