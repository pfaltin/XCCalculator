import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("637d40c7-1480-4e83-9fd5-793aef24e12f")
public class TockaGFS extends Tocka {
    @objid ("8f451b35-1929-43a6-a9ee-8bcc17022b87")
    public int vrijeme;

    @objid ("aedce33c-bed6-49a1-bea6-1c6d6fdec6d4")
    public String datum;

    @objid ("4c001bc4-5ade-49ca-831e-b66e5237be68")
    public List<slojZraka> slojZraka = new ArrayList<slojZraka> ();

}
