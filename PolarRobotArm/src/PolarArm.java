import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.TextureLoader;
import javax.media.j3d.*;
//import javax.swing.*;
import java.awt.*;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.Vector;
import javax.media.j3d.Transform3D;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.vecmath.Color3f;
import javax.vecmath.Point2f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

public class PolarArm extends JFrame implements ActionListener, KeyListener {

   
    BranchGroup wezel_scena = new BranchGroup();
    TransformGroup obrot_animacja = new TransformGroup();
    BoundingSphere boundso;
    SimpleUniverse simpleU;
    OrbitBehavior orbita;

    JButton reset_kamery = new JButton();
    JButton zacznij_nagrywanie = new JButton();
    JButton zakoncz_nagrywanie = new JButton();
    JButton odtworz_nagranie = new JButton();
    JButton ustawKoordynaty = new JButton();

    JTextArea textObrot1 = new JTextArea();
    JTextArea textObrot2 = new JTextArea();
    JTextArea textWysuniecie = new JTextArea();
    
    TransformGroup segment = new TransformGroup();
    Transform3D przesuniecie_seg = new Transform3D();
    TransformGroup segment2 = new TransformGroup();
    Transform3D przesuniecie_seg2 = new Transform3D();
    TransformGroup segment3 = new TransformGroup();
    Transform3D przesuniecie_seg3 = new Transform3D();
    TransformGroup segment4 = new TransformGroup();
    Transform3D przesuniecie_seg4 = new Transform3D();
    TransformGroup ramie_p1 = new TransformGroup();
    Transform3D przesuniecie_ram = new Transform3D();
    TransformGroup tg_kulka = new TransformGroup();
    Transform3D t3d_kulka = new Transform3D();
        
    TransformGroup segment5 = new TransformGroup();
    Transform3D przesuniecie_seg5 = new Transform3D();
    TransformGroup ramie_p2 = new TransformGroup();
    Transform3D przesuniecie_ramie2 = new Transform3D();
    Transform3D tg_kulka_nag = new Transform3D();

    BranchGroup kulkaBranch = new BranchGroup();
    
    boolean key_a;
    boolean key_d;
    boolean key_w;
    boolean key_s;
    boolean key_q;
    boolean key_e;
    
    float wysuniecie=0.0f;
    float kat_wychylenia=0.0f;
    float kat_obrotu=0.0f;

    PolarArm(){
         super("Polar Robot Arm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        canvas3D.setPreferredSize(new Dimension(1280,720));
        canvas3D.addKeyListener(this);
        //canvas3D.add(new Keyboard());
        add(canvas3D);
        pack();
        add(BorderLayout.EAST, stworzPanelPrzyciskow());
        add(BorderLayout.NORTH, dodanieInstrukcji());
        add(BorderLayout.CENTER, canvas3D);
        setVisible(true);
        

         BranchGroup scena = new BranchGroup();
        scena = utworzScene();
        scena.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	scena.compile();
        
        simpleU = new SimpleUniverse(canvas3D);
        ViewingPlatform viewingPlatform = simpleU.getViewingPlatform();
        
        
        orbita = new OrbitBehavior(canvas3D,
						OrbitBehavior.REVERSE_ALL);
	boundso = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
						   100.0);
	orbita.setSchedulingBounds(boundso);
	viewingPlatform.setViewPlatformBehavior(orbita);

        Transform3D przesuniecie_obserwatora = new Transform3D();
        Transform3D rot_obs = new Transform3D();
        rot_obs.rotY((float)(-Math.PI/7));
        przesuniecie_obserwatora.set(new Vector3f(-1.2f,2.0f,2.0f));
        przesuniecie_obserwatora.mul(rot_obs);
        rot_obs.rotX((float)(-Math.PI/6));
        przesuniecie_obserwatora.mul(rot_obs);

        simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);

        simpleU.addBranchGraph(scena);

    }
    public static JPanel dodanieInstrukcji() {
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon("src\\instrukcja_robota.jpg"));
        JPanel panel_instrukcji = new JPanel(new FlowLayout());
        panel_instrukcji.add(label);
        return panel_instrukcji;
    }
    
    public JPanel stworzPanelPrzyciskow() {
        JPanel panel_menu = new JPanel(new GridLayout(11, 1, 10, 10));

        reset_kamery.setText("Reset Kamery");
        reset_kamery.addActionListener(this);

        zacznij_nagrywanie.setText("Rozpocznij nagrywanie");
        zacznij_nagrywanie.addActionListener(this);

        zakoncz_nagrywanie.setText("Zakoncz nagrywanie");
        zakoncz_nagrywanie.addActionListener(this);

        odtworz_nagranie.setText("Odtworz nagranie");
        odtworz_nagranie.addActionListener(this);
        
        textObrot1 = new JTextArea(1,5);
        textObrot1.setFont(new Font("Tahoma",Font.BOLD,40));
        textObrot1.setText("0.00");
        textObrot2 = new JTextArea(1,5);
        textObrot2.setFont(new Font("Tahoma",Font.BOLD,40));
        textObrot2.setText("0.00");
        textWysuniecie = new JTextArea(1,5);
        textWysuniecie.setFont(new Font("Tahoma",Font.BOLD,40));
        textWysuniecie.setText("0.00");
        ustawKoordynaty.setText("Ustaw Koordynaty");
        ustawKoordynaty.addActionListener(this);
        
        

        panel_menu.add(reset_kamery);
        panel_menu.add(zacznij_nagrywanie);
        panel_menu.add(zakoncz_nagrywanie);
        panel_menu.add(odtworz_nagranie);
        panel_menu.add(new JLabel("Kąt robota (-360°,360°):"));
        panel_menu.add(textObrot1);
        panel_menu.add(new JLabel("Kąt ramienia [-45°,45°]:"));
        panel_menu.add(textObrot2);
        panel_menu.add(new JLabel("Wysuniecie ramienia [%]:"));
        panel_menu.add(textWysuniecie);
        panel_menu.add(ustawKoordynaty);
        return panel_menu;
    }
    BranchGroup utworzScene()
    {

        int i;

        
        wezel_scena.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        wezel_scena.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        
        
        obrot_animacja.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        wezel_scena.addChild(obrot_animacja);
      

        Alpha alpha_animacja = new Alpha(-1,50000);
      //Alpha alpha_animacja2 = new Alpha(-1,Alpha.DECREASING_ENABLE,0,0,0,0,0,5000,0,0);

        RotationInterpolator obracacz = new RotationInterpolator(alpha_animacja, obrot_animacja);
   
        
        BoundingSphere bounds = new BoundingSphere();
        obracacz.setSchedulingBounds(bounds);
        obrot_animacja.addChild(obracacz);

        Appearance wyglad_ziemia = new Appearance();
        Appearance wyglad_mury   = new Appearance();
        //Appearance wyglad_daszek = new Appearance();

        Texture tekstura_nieba = new TextureLoader("obrazki/clouds.gif", null, new Container()).getTexture();
        Appearance wyglad_niebo = new Appearance();
        wyglad_niebo.setTexture(tekstura_nieba);
        wyglad_niebo.setTextureAttributes(new TextureAttributes());
        Sphere niebo = new Sphere(5.0f,
        Primitive.GENERATE_NORMALS_INWARD + Primitive.GENERATE_TEXTURE_COORDS,
        wyglad_niebo);
        obrot_animacja.addChild(niebo);
        //Material wmaterial_daszek = new Material(new Color3f(0.0f, 0.1f,0.0f), new Color3f(0.3f,0.0f,0.3f),
          //                                   new Color3f(0.6f, 0.1f, 0.1f), new Color3f(1.0f, 0.5f, 0.5f), 80.0f);
        //wyglad_daszek.setMaterial(wmaterial_daszek);

        TextureLoader loader = new TextureLoader("obrazki/beton.jpg",null);
        ImageComponent2D image = loader.getImage();

        Texture2D podloga = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                        image.getWidth(), image.getHeight());

        podloga.setImage(0, image);
        podloga.setBoundaryModeS(Texture.WRAP);
        podloga.setBoundaryModeT(Texture.WRAP);


        loader = new TextureLoader("obrazki/black.jpg",this);
        image = loader.getImage();

        Texture2D murek = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                        image.getWidth(), image.getHeight());
        murek.setImage(0, image);
        murek.setBoundaryModeS(Texture.WRAP);
        murek.setBoundaryModeT(Texture.WRAP);

        

        //BoundingSphere bounds = new BoundingSphere();
        AmbientLight lightA = new AmbientLight();
        lightA.setInfluencingBounds(bounds);
        wezel_scena.addChild(lightA);

        DirectionalLight lightD = new DirectionalLight();
        lightD.setInfluencingBounds(bounds);
        lightD.setDirection(new Vector3f(0.0f, 0.0f, -1.0f));
        lightD.setColor(new Color3f(1.0f, 1.0f, 1.0f));
        wezel_scena.addChild(lightD);


        wyglad_ziemia.setTexture(podloga);
        wyglad_mury.setTexture(murek);

        Point3f[]  coords = new Point3f[4];
        for(i = 0; i< 4; i++)
            coords[i] = new Point3f();

        Point2f[]  tex_coords = new Point2f[4];
        for(i = 0; i< 4; i++)
            tex_coords[i] = new Point2f();

        coords[0].y = 0.0f;
        coords[1].y = 0.0f;
        coords[2].y = 0.0f;
        coords[3].y = 0.0f;

        coords[0].x = 3.0f;
        coords[1].x = 3.0f;
        coords[2].x = -3.0f;
        coords[3].x = -3.0f;

        coords[0].z = 3.0f;
        coords[1].z = -3.0f;
        coords[2].z = -3.0f;
        coords[3].z = 3.0f;

        tex_coords[0].x = 0.0f;
        tex_coords[0].y = 0.0f;

        tex_coords[1].x = 10.0f;
        tex_coords[1].y = 0.0f;

        tex_coords[2].x = 0.0f;
        tex_coords[2].y = 10.0f;

        tex_coords[3].x = 10.0f;
        tex_coords[3].y = 0.0f;

        
        //ziemia

        QuadArray qa_ziemia = new QuadArray(4, GeometryArray.COORDINATES|
                GeometryArray.TEXTURE_COORDINATE_2);
        qa_ziemia.setCoordinates(0,coords);

        qa_ziemia.setTextureCoordinates(0, tex_coords);


        Shape3D ziemia = new Shape3D(qa_ziemia);
        ziemia.setAppearance(wyglad_ziemia);

        wezel_scena.addChild(ziemia);
        

        //podstawa
        float podstawa_szer = 0.3f;
        //float podstawa_dl = 0.4f;
        float podstawa_wys = 0.05f;
        
        //murowana podstawa
        
        Appearance wyglad_muru = new Appearance();
        Texture tekstura_muru = new TextureLoader("obrazki/murek.gif", this).getTexture();
        wyglad_muru.setTexture(tekstura_muru);
        Transform3D przesuniecie_muru = new Transform3D();
        przesuniecie_muru.setTranslation(new Vector3f(0.0f, podstawa_wys/2, 0.0f));
        TransformGroup mur_p = new TransformGroup(przesuniecie_muru);
        Box MurModel = new Box(podstawa_szer, podstawa_wys, podstawa_szer, Box.GENERATE_TEXTURE_COORDS, wyglad_muru);
        
 
 
        mur_p.addChild(MurModel);
        
        wezel_scena.addChild(mur_p);
        
        
        //podstawa robota
        float wys_seg1=0.1f;

        segment2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        segment2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        segment2.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        segment2.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        segment.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        segment3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        segment4.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        ramie_p1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        ramie_p2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        przesuniecie_seg.set(new Vector3f(0.0f,podstawa_wys + wys_seg1/2,0.0f));
        segment.setTransform(przesuniecie_seg);

        Cylinder walec = new Cylinder(podstawa_szer-0.05f,wys_seg1,Cylinder.GENERATE_NORMALS| Cylinder.GENERATE_TEXTURE_COORDS, wyglad_mury);
        segment.addChild(walec);
        wezel_scena.addChild(segment);
        
        //ten główny słup
        float wys_seg2 = 0.6f;
        
        przesuniecie_seg2.set(new Vector3f(0.0f,podstawa_wys + wys_seg1 + wys_seg2/2,0.0f));
        segment2.setTransform(przesuniecie_seg2);

        Cylinder walec2 = new Cylinder(podstawa_szer-0.15f,wys_seg2,Cylinder.GENERATE_NORMALS| Cylinder.GENERATE_TEXTURE_COORDS, wyglad_mury);
        segment2.addChild(walec2);
        wezel_scena.addChild(segment2);
        
        //te koła z boku
        
        float gr_kola = 0.05f;
        float promien_kola = 0.15f;
        
        przesuniecie_seg3.set(new Vector3f(podstawa_szer/2-0.02f,wys_seg2/2+promien_kola/2,0.0f));
        Transform3D  tmp_rot      = new Transform3D();
        tmp_rot.rotZ(Math.PI/2);
        przesuniecie_seg3.mul(tmp_rot);
        segment3.setTransform(przesuniecie_seg3);

        Cylinder walec3 = new Cylinder(promien_kola,gr_kola,Cylinder.GENERATE_NORMALS| Cylinder.GENERATE_TEXTURE_COORDS, wyglad_mury);
        segment3.addChild(walec3);
        segment2.addChild(segment3);
        
        przesuniecie_seg4.set(new Vector3f(-podstawa_szer/2+0.02f,wys_seg2/2+promien_kola/2,0.0f));
        przesuniecie_seg4.mul(tmp_rot);
        segment4.setTransform(przesuniecie_seg4);
        Cylinder walec4 = new Cylinder(promien_kola,gr_kola,Cylinder.GENERATE_NORMALS| Cylinder.GENERATE_TEXTURE_COORDS, wyglad_mury);
        segment4.addChild(walec4);
        segment2.addChild(segment4);
        
        //główne ramię
        
        Box ramie1 = new Box(podstawa_szer-0.2f, podstawa_wys, podstawa_szer+0.1f, Box.GENERATE_TEXTURE_COORDS, wyglad_mury);
        
        przesuniecie_ram.set(new Vector3f(0.0f, wys_seg2/2+promien_kola/2, 0.0f));
        
        ramie_p1.setTransform(przesuniecie_ram);
        
        //Appearance wyglad_ram = new Appearance();
        //Texture tekstura_muru = new TextureLoader("obrazki/murek.gif", this).getTexture();
        //wyglad_muru.setTexture(tekstura_muru);
 
 
        ramie_p1.addChild(ramie1);
        
        segment2.addChild(ramie_p1);
        
        Appearance wyglad_alum = new Appearance();
        Texture tekstura_alum = new TextureLoader("obrazki/alum.jpg", this).getTexture();
        wyglad_alum.setTexture(tekstura_alum);
        
        //ten aluminiowy walec, wokół którego będzie wykonywany obrót w górę i w dół
        
        przesuniecie_seg5.set(new Vector3f(0.0f,0.0f,0.0f));
        przesuniecie_seg5.mul(tmp_rot);
        segment5.setTransform(przesuniecie_seg5);
        Cylinder walec5 = new Cylinder(0.1f,podstawa_szer+0.02f,Cylinder.GENERATE_NORMALS| Cylinder.GENERATE_TEXTURE_COORDS, wyglad_alum);
        segment5.addChild(walec5);
        ramie_p1.addChild(segment5);
        
        
        //wysuwane ramię aluminiowe
        
        przesuniecie_ramie2.set(new Vector3f(0.0f,0.0f,0.2f));
        Transform3D  tmp_rot2 = new Transform3D();
        tmp_rot2.rotX(Math.PI/2);
        przesuniecie_ramie2.mul(tmp_rot2);
        ramie_p2.setTransform(przesuniecie_ramie2);
        Cylinder ramie2 = new Cylinder(0.03f,podstawa_szer+0.7f,Cylinder.GENERATE_NORMALS| Cylinder.GENERATE_TEXTURE_COORDS, wyglad_alum);
        ramie_p2.addChild(ramie2);
        ramie_p1.addChild(ramie_p2);


        /// kulka 
     Material kulkowy = new Material();
        kulkowy.setEmissiveColor(0.80f, 0.1f, 0.26f);
        kulkowy.setDiffuseColor(0.32f, 0.21f, 0.08f);
        kulkowy.setSpecularColor(0.45f, 0.32f, 0.21f);
        kulkowy.setShininess(38f);

        Appearance stylKulka = new Appearance();
        stylKulka.setMaterial(kulkowy);

        Transform3D t3d_przesuniecie = new Transform3D();
        tg_kulka.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tg_kulka.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        t3d_przesuniecie.set(new Vector3f(0.0f, 0.2f, 1.0f)); // przesuwam obiekt z orgin na miejsce
        t3d_kulka.mul(t3d_przesuniecie);
        tg_kulka.setTransform(t3d_przesuniecie);
        Sphere kulka = new Sphere(0.2f, stylKulka);
        tg_kulka.addChild(kulka);
        tg_kulka.setUserData("kulka");

        kulkaBranch.setCapability(BranchGroup.ALLOW_DETACH);
        kulkaBranch.addChild(tg_kulka);
        wezel_scena.addChild(kulkaBranch);
        
        return wezel_scena;


    }

    public static void main(String args[]){
      new PolarArm();

   }
    
    public void czekaj(){
        try {
            Thread.sleep(20);
      } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
      }
    }
    
    public void wykonajRuch(){
        
        
        Transform3D akcja = new Transform3D();
        
        
        if (key_a) {
            akcja.rotY(Math.PI / 100);
            kat_obrotu+=Math.PI / 100;
            kat_obrotu%=2*Math.PI;
            przesuniecie_seg2.mul(akcja);
            segment2.setTransform(przesuniecie_seg2);
            
        }
        if (key_w  && kat_wychylenia < Math.PI/4) {
            akcja.rotX(Math.PI / 100);
            kat_wychylenia+= Math.PI / 100;
            przesuniecie_ram.mul(akcja);
            ramie_p1.setTransform(przesuniecie_ram);
            
        }
        if (key_s  && kat_wychylenia > -Math.PI/4) {
            akcja.rotX(-Math.PI / 100);
            kat_wychylenia-= Math.PI / 100;
            przesuniecie_ram.mul(akcja);
            ramie_p1.setTransform(przesuniecie_ram);
            
        }
        if (key_d) {
            akcja.rotY(-Math.PI / 100);
            kat_obrotu-=Math.PI / 100;
            kat_obrotu%=2*Math.PI;
            przesuniecie_seg2.mul(akcja);
            segment2.setTransform(przesuniecie_seg2);
            
        }
        if (key_q && wysuniecie<0.59f) {
            akcja.set(new Vector3f(0.0f,0.01f,0.0f));
            wysuniecie+=0.01f;
            przesuniecie_ramie2.mul(akcja);
            ramie_p2.setTransform(przesuniecie_ramie2);
            
        }
        if (key_e && wysuniecie > 0.01f) {
           akcja.set(new Vector3f(0.0f,-0.01f,0.0f));
           wysuniecie-=0.01f;
           przesuniecie_ramie2.mul(akcja);
           ramie_p2.setTransform(przesuniecie_ramie2);
            
        }
        textObrot1.setText(String.format("%.2f", kat_obrotu/Math.PI*180));
        textObrot2.setText(String.format("%.2f", kat_wychylenia/Math.PI*180));
        textWysuniecie.setText(String.format("%.2f", wysuniecie/0.6 * 100));
    }

    public void actionPerformed(ActionEvent e) {
        KeyEvent klawisz_Q = new KeyEvent(new Button(), 1, 20, 1, KeyEvent.VK_Q, 'Q');
        KeyEvent klawisz_E = new KeyEvent(new Button(), 1, 20, 1, KeyEvent.VK_E, 'E');
        KeyEvent klawisz_A = new KeyEvent(new Button(), 1, 20, 1, KeyEvent.VK_A, 'A');
        KeyEvent klawisz_D = new KeyEvent(new Button(), 1, 20, 1, KeyEvent.VK_D, 'D');
        KeyEvent klawisz_W = new KeyEvent(new Button(), 1, 20, 1, KeyEvent.VK_W, 'W');
        KeyEvent klawisz_S = new KeyEvent(new Button(), 1, 20, 1, KeyEvent.VK_S, 'S');
        if (e.getSource() == reset_kamery) {
            Transform3D przesuniecie_obserwatora = new Transform3D();
            Transform3D rot_obs = new Transform3D();
            rot_obs.rotY((float)(-Math.PI/7));
            przesuniecie_obserwatora.set(new Vector3f(-1.2f,2.0f,2.0f));
            przesuniecie_obserwatora.mul(rot_obs);
            rot_obs.rotX((float)(-Math.PI/6));
            przesuniecie_obserwatora.mul(rot_obs);

            simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);
        }
        if (e.getSource() == ustawKoordynaty) {
            float celWysuniecia = Float.parseFloat(textWysuniecie.getText()) % 101;
            float celObrotu1 = Float.parseFloat(textObrot1.getText()) % 360;
            float celObrotu2 = Float.parseFloat(textObrot2.getText()) % 46;
            
            
            while(kat_obrotu/Math.PI*180<celObrotu1-1){
                keyPressed(klawisz_A);
                keyReleased(klawisz_A);
                czekaj();
            }
            while(kat_obrotu/Math.PI*180>celObrotu1+1){
                keyPressed(klawisz_D);
                keyReleased(klawisz_D);
                czekaj();
            }
            while(kat_wychylenia/Math.PI*180<celObrotu2-1){
                keyPressed(klawisz_W);
                keyReleased(klawisz_W);
                czekaj();
            }
            while(kat_wychylenia/Math.PI*180>celObrotu2+1){
                keyPressed(klawisz_S);
                keyReleased(klawisz_S);
                czekaj();
            }
            while(wysuniecie/0.6*100<celWysuniecia-1){
                keyPressed(klawisz_Q);
                keyReleased(klawisz_Q);
                czekaj();
            }
            while(wysuniecie/0.6*100>celWysuniecia){
                keyPressed(klawisz_E);
                keyReleased(klawisz_E);
                czekaj();
            }
            
        }
    }

    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A: 
                
                key_a = true;
                break;
            case KeyEvent.VK_D:
                key_d = true;
                break;
            case KeyEvent.VK_W: 
                key_w = true;
                break;
            case KeyEvent.VK_S:
                key_s = true;
                break;
            case KeyEvent.VK_Q:
                key_q = true;
                break;
            case KeyEvent.VK_E:
                key_e = true;
                break;
        }
        wykonajRuch();
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                key_a = false;
                break;
            case KeyEvent.VK_D:
                key_d = false;
                break;
            case KeyEvent.VK_W:
                key_w = false;
                break;
            case KeyEvent.VK_S:
                key_s = false;
                break;
            case KeyEvent.VK_Q:
                key_q = false;
                break;
            case KeyEvent.VK_E:
                key_e = false;
                break;
        }
    }

}
