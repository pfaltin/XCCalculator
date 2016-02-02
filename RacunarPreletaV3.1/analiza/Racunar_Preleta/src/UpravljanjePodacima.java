import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("10550413-3da0-4d68-8e8f-67d39cef19a0")
public class UpravljanjePodacima {
    @objid ("a59ea2af-ca55-4b6d-97b9-b82297dd90e1")
    public List<Tocka> tocka = new ArrayList<Tocka> ();

    @objid ("a0dc6429-9c27-46c1-8034-e74776e1768d")
    public List<Ruta> ruta = new ArrayList<Ruta> ();

    @objid ("718be8e8-d868-467d-bb3f-695878c96fa3")
    public List<Zrakoplov> zrakoplov = new ArrayList<Zrakoplov> ();

    @objid ("0e7b898f-d781-4761-81e4-f55f255ddb9b")
    public List<Karta> karta = new ArrayList<Karta> ();

}
