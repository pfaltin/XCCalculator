import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("85fc3a94-141a-4e66-a61c-6c726ac56f79")
public class Ruta {
    @objid ("a7448217-2ac5-4e49-a0b2-d8059a04fcf7")
    public String naziv;

    @objid ("3de89987-2b45-4b4b-ab74-2b667efa85c1")
    public List<Tocka> tocka = new ArrayList<Tocka> ();

}
