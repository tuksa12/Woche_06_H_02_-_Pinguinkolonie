package pgdp.colony;

import pgdp.helper.DynamicClass;
import pgdp.helper.DynamicConstructor;
import pgdp.helper.DynamicMethod;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

import static pgdp.MiniJava.*;

public class ColonySimulator {
    // simulation settings
    ColonyRenderer renderer;
    int targetFrametime = 1000; // frametime in miliseconds that we want to achieve
    boolean pause = true, simulateYearly = false;
    int pauseOnDay = -1; // will pause if current day equals this, set to <1 to not use

    // colony info
    PenguinColony colony;
    int day;
    double avgLifespan, avgSize, avgMaxSize, avgSkill;
    List<Penguin> penguinList = new ArrayList<>(), deadList = new ArrayList<>(), babyList;


    // useful colony presets and creators to call in main
    void setSeed(int seed) {
        write("Seed = " + seed);
        setRandomWithSeed(seed, null);
    }

    void setRandomSeed() {
        int seed = (int) System.currentTimeMillis();
        write("Seed = " + seed);
        setRandomWithSeed(seed, null);
    }

    void createCustomPenguins(int num) {
        for (int i = 0; i < num; i++)
            Student.createPenguin.invokeOn(colony, Student.newGenome(readString("Enter the DNA sequence of length 13:")));
    }

    void createRandomPenguins(int num) {
        for (int i = 0; i < num; i++)
            write(Student.createPenguin.invokeOn(colony, Student.newGenome()).toString());
    }

    void createAdamAndEve() {
        write(Student.createPenguin.invokeOn(colony, Student.newGenome("TTTTCTTTTAATT")).toString());
        write(Student.createPenguin.invokeOn(colony, Student.newGenome("TTTTCTTTTAAAG")).toString());
    }

    void createPastelPenguins() {
        write(Student.createPenguin.invokeOn(colony, Student.newGenome("TTCTCTTTTCCGT")).toString());
        write(Student.createPenguin.invokeOn(colony, Student.newGenome("TTTTCTTTTAACG")).toString());
    }

    void createAlbinoPenguins() {
        write(Student.createPenguin.invokeOn(colony, Student.newGenome("TTAACTATTTTTT")).toString());
        write(Student.createPenguin.invokeOn(colony, Student.newGenome("TTAAGACACTTTG")).toString());
    }


    // main method, experiment with this how ever you like
    public static void main(String[] args) {
        if (!Student.isComplete()) {
            write("Please complete all tasks first before using this simulator!");
            return;
        }

        PenguinColony colony = Student.newPenguinColony();
        ColonySimulator simulator = new ColonySimulator(colony, true);

        simulator.setSeed(42); //or use simulator.setRandomSeed();

        simulator.createAdamAndEve();
        //simulator.createPastelPenguins();
        //simulator.createAlbinoPenguins();
        //simulator.createRandomPenguins(2);
        //simulator.createCustomPenguins(2);

        simulator.simulate();
    }

    ColonySimulator(PenguinColony colony, boolean shouldRender) {
        this.colony = colony;
        if (shouldRender) {
            renderer = new ColonyRenderer(this);
            renderer.setup();
        } else {
            renderer = null;
            pause = false;
        }
    }

    void simulate() {

        try {
            // day 0
            penguinList.addAll(Student.getPopulation.invokeOn(colony).list);
            day = Student.getTime.invokeOn(colony);
            avgLifespan = Math.round(penguinList.stream().filter(p -> p != null)
                    .mapToDouble(p -> Student.lifespan.invokeOn(Student.getGenome.invokeOn(p)))
                    .average().orElse(0));
            avgSize = Math.round(penguinList.stream().filter(p -> p != null)
                    .mapToDouble(p -> Student.getSize.invokeOn(p))
                    .average().orElse(0));
            avgMaxSize = Math.round(penguinList.stream().filter(p -> p != null)
                    .mapToDouble(p -> Student.maxSize.invokeOn(Student.getGenome.invokeOn(p)))
                    .average().orElse(0));
            avgSkill = Math.round(penguinList.stream().filter(p -> p != null)
                    .mapToDouble(p -> Student.skill.invokeOn(Student.getGenome.invokeOn(p)))
                    .average().orElse(0));

            write("Initial Average Values:");
            write("Avg. Lifespan: " + avgLifespan);
            write("Avg. Size: " + avgSize + "/" + avgMaxSize);
            write("Avg. Skill: " + avgSkill);

            if (renderer != null)
                renderer.render();

            // simulate colony
            while (true) {
                // wait until unpaused
                while (pause && day != pauseOnDay) {
                    Thread.sleep(50);
                }

                long before = System.currentTimeMillis();

                // simulate colony and update stats
                if (simulateYearly) {
                    do {
                        Student.simulateDay.invokeOnVoid(colony);
                    } while (Student.getTime.invokeOn(colony) % PenguinColony.DAYS_PER_YEAR != 0);
                } else
                    Student.simulateDay.invokeOnVoid(colony);

                day = Student.getTime.invokeOn(colony);
                List<Penguin> newList = Student.getPopulation.invokeOn(colony).list;

                // calculate averages
                avgLifespan = Math.round(penguinList.stream().filter(p -> p != null)
                        .mapToDouble(p -> Student.lifespan.invokeOn(Student.getGenome.invokeOn(p)))
                        .average().orElse(0));
                avgSize = Math.round(penguinList.stream().filter(p -> p != null)
                        .mapToDouble(p -> Student.getSize.invokeOn(p))
                        .average().orElse(0));
                avgMaxSize = Math.round(penguinList.stream().filter(p -> p != null)
                        .mapToDouble(p -> Student.maxSize.invokeOn(Student.getGenome.invokeOn(p)))
                        .average().orElse(0));
                avgSkill = Math.round(penguinList.stream().filter(p -> p != null)
                        .mapToDouble(p -> Student.skill.invokeOn(Student.getGenome.invokeOn(p)))
                        .average().orElse(0));

                // bury the dead penguins from yesterday
                for (Penguin p : deadList)
                    penguinList.set(penguinList.indexOf(p), null);

                // find new dead penguins and remember them
                deadList = new ArrayList<>();
                for (Penguin p : penguinList)
                    if (p != null && !newList.contains(p))
                        deadList.add(p);

                // find new born penguins
                babyList = new ArrayList<>();
                for (Penguin p : newList)
                    if (!penguinList.contains(p))
                        babyList.add(p);

                // find some free space for new born penguins
                int nextFree = penguinList.indexOf(null); // -1 if full
                for (Penguin p : babyList) {
                    if (nextFree == -1) {
                        penguinList.add(p);
                    } else {
                        penguinList.set(nextFree, p);

                        // find next free space
                        while (nextFree < penguinList.size() && penguinList.get(nextFree) != null)
                            nextFree++;
                        nextFree = (nextFree == penguinList.size() ? -1 : nextFree); // set to -1 if full
                    }
                }

                // render colony
                if (renderer != null)
                    renderer.render();

                // slow down
                long frametime = System.currentTimeMillis() - before;
                long sleepTime = targetFrametime - frametime;

                if (sleepTime > 0 && !pause)
                    Thread.sleep(sleepTime);
            }
        } catch (InterruptedException ie) {
            /* Intentionally left blank */
        }
    }

}

class ColonyRenderer {
    private ColonySimulator simulator;
    private JFrame frame;
    private JPanel penguGrid;
    private GridLayout penguGridLayout;
    private JLabel day, population, avgLifespan, avgSize, avgSkill; //topbar labels
    private JLabel status, speed; //bottombar labels
    private Image pengu, penguF, penguDead, fish, health, baby;

    ColonyRenderer(ColonySimulator simulator) {
        this.simulator = simulator;
    }

    void setup() {
        // load images
        try {
            pengu = ImageIO.read(simulator.getClass().getResource("img/penguin.png"));
            penguF = ImageIO.read(simulator.getClass().getResource("img/penguin_female.png"));
            penguDead = ImageIO.read(simulator.getClass().getResource("img/penguin_dead.png"));
            fish = ImageIO.read(simulator.getClass().getResource("img/fish.png"))
                    .getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            health = ImageIO.read(simulator.getClass().getResource("img/health.png"))
                    .getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            baby = ImageIO.read(simulator.getClass().getResource("img/baby.png"))
                    .getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        } catch (IOException | IllegalArgumentException e) {
            write("Bild-Datei nicht gefunden! " + e);
        }

        // init frame and layout
        frame = new JFrame("Penguin Colony");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setLayout(new BorderLayout());
        penguGrid = new JPanel(penguGridLayout = new GridLayout(0, 6, 0, 20));
        penguGrid.setBackground(Color.white);

        // keybindings
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                switch (ke.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        frame.dispose();
                        System.exit(0);
                        break;
                    case KeyEvent.VK_UP:
                        simulator.targetFrametime = Math.max(0,
                                simulator.targetFrametime - 250);
                        speed.setText("  " + simulator.targetFrametime + "ms/"
                                + (simulator.simulateYearly ? "Penguin-Year  |" : "Penguin-Day  |"));
                        break;
                    case KeyEvent.VK_DOWN:
                        simulator.targetFrametime += 250;
                        speed.setText("  " + simulator.targetFrametime + "ms/"
                                + (simulator.simulateYearly ? "Penguin-Year  |" : "Penguin-Day  |"));
                        break;
                    case KeyEvent.VK_ENTER:
                        if (simulator.pause)
                            simulator.pauseOnDay = simulator.day;
                        break;
                    case KeyEvent.VK_SPACE:
                        simulator.pauseOnDay = 0;
                        simulator.pause = !simulator.pause;
                        if (!simulator.pause)
                            simulator.pauseOnDay = 0;
                        status.setText(simulator.pause ? "  Paused  |" : "  Running...  |");
                        break;
                    case KeyEvent.VK_Y:
                        simulator.simulateYearly = !simulator.simulateYearly;
                        speed.setText("  " + simulator.targetFrametime + "ms/"
                                + (simulator.simulateYearly ? "Penguin-Year  |" : "Penguin-Day  |"));
                    default:
                        break;
                }
            }
        });

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                penguGridLayout.setColumns(Math.max(1, penguGrid.getWidth() / 100));
            }
        });

        JScrollPane scrollPane = new JScrollPane(penguGrid, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // scrolling speed, set to your taste
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // topbar
        JPanel topbar = new JPanel();
        topbar.setLayout(new BoxLayout(topbar, BoxLayout.X_AXIS));

        topbar.add(day = new JLabel(" Day 0: "));
        topbar.add(population = new JLabel(" Population: 0  "));
        topbar.add(avgLifespan = new JLabel("  Avg. Lifespan: 0  "));
        topbar.add(avgSize = new JLabel("  Avg. Size: 0-0-0  "));
        topbar.add(avgSkill = new JLabel("  Avg. Skill: 0-0  "));

        frame.add(topbar, BorderLayout.NORTH);

        // controls
        JPanel bottombar = new JPanel();
        bottombar.setLayout(new BoxLayout(bottombar, BoxLayout.X_AXIS));

        bottombar.add(status = new JLabel("  Paused  |"));
        bottombar.add(speed = new JLabel("  1000ms/Penguin-Day  |"));
        bottombar.add(new JLabel("  Enter: Simulate 1 Step (while paused)"
                + "  |  Space: Toggle Pause/Unpause"
                + "  |  Up Arrow: Simulate Faster"
                + "  |  Down Arrow: Simulate Slower"
                + "  |  Y: Toggle Daily/Yearly Simulation"));

        frame.add(bottombar, BorderLayout.SOUTH);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        penguGridLayout.setColumns(Math.max(1, penguGrid.getWidth() / 100));
        frame.setVisible(true);
    }

    void render() {
        // update topbar
        day.setText(" Day " + simulator.day + ": ");
        population.setText("  Population: " + Student.getPopulation.invokeOn(simulator.colony).size() + "  ");
        avgLifespan.setText("  Avg. Lifespan: " + simulator.avgLifespan + "  ");
        avgSize.setText("  Avg. Size: " + simulator.avgSize + "/" + simulator.avgMaxSize + "  ");
        avgSkill.setText("  Avg. Skill: " + simulator.avgSkill + "  ");

        // add missing penguPanels
        int missing = simulator.penguinList.size() - penguGrid.getComponentCount();
        for (int i = 0; i < missing; i++)
            penguGrid.add(new PenguPanel());

        // update all panels
        for (int i = 0; i < simulator.penguinList.size(); i++) {
            ((PenguPanel) penguGrid.getComponent(i))
                    .updatePengu(simulator.penguinList.get(i),
                            Student.getTime.invokeOn(simulator.colony) % PenguinColony.DAYS_PER_YEAR == 0);
        }

        penguGridLayout.setColumns(Math.max(1, penguGrid.getWidth() / 100));
        frame.revalidate();
        frame.repaint();
    }

    private class PenguPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private boolean active = false;
        private Penguin myPengu;

        private JLabel newborn;
        private JLabel image;
        private JLabel name;
        private JLabel hp;
        private JLabel myFish;
        private JLabel child;

        public PenguPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.WHITE);
        }

        public void setup(Penguin p) {
            active = true;
            myPengu = p;

            add(Box.createVerticalGlue());

            add(newborn = new JLabel());

            image = new JLabel();
            image.setOpaque(true);
            add(image);

            add(name = new JLabel());
            add(hp = new JLabel(new ImageIcon(health), JLabel.LEFT));
            add(myFish = new JLabel(new ImageIcon(fish), JLabel.LEFT));
            add(child = new JLabel());
            child.setHorizontalAlignment(JLabel.LEFT);
        }

        public void updatePengu(Penguin p, boolean newborn) {
            if (p == null || p != myPengu) {
                removeAll();
                active = false;
                if (p == null)
                    return;
            }

            if (!active)
                setup(p);

            Genome genome = Student.getGenome.invokeOn(p);

            if (Student.getAge.invokeOn(p) == 0 && newborn)
                this.newborn.setText("New!");
            else
                this.newborn.setText(null);

            // image
            Byte color = Student.color.invokeOn(genome);
            float r = (color >> 4) / 3.0f, g = (color >> 2) % 4 / 3.0f, b = (color % 4) / 3.0f;
            image.setBackground(new Color(r, g, b));

            int size = Student.getSize.invokeOn(p) * 100 / 32;
            if (Student.getHealth.invokeOn(p) > 0)
                image.setIcon(new ImageIcon((Student.isMale.invokeOn(genome) ? pengu : penguF)
                        .getScaledInstance(size, size, Image.SCALE_SMOOTH)));
            else
                image.setIcon(new ImageIcon(penguDead.getScaledInstance(size, size, Image.SCALE_SMOOTH)));

            // stats
            name.setText(Student.getName.invokeOn(p) + " (" + Student.getAge.invokeOn(p) + (Student.isMale.invokeOn(genome) ? "M" : "F") + ")");
            hp.setText(Integer.toString(Math.max(0, Student.getHealth.invokeOn(p))));
            myFish.setText(Integer.toString(Student.getNumFish.invokeOn(p)));

            // child
            if (Student.getChild.invokeOn(p) != null) {
                child.setText(Student.getName.invokeOn(Student.getChild.invokeOn(p)));
                child.setIcon(new ImageIcon(baby));
            } else {
                child.setText(" ");
                child.setIcon(null);
            }

            image.setToolTipText("<html>"
                    + Student.toString.invokeOn(p).replaceAll("(; |: )", "<br/>").replaceAll(";", "")
                    + "</html>");
        }
    }
}


//Student Code Checker and Caller, assumes the classes already exist
class Student {
    //Genome
    static final DynamicClass<?> Genome = DynamicClass.toDynamic("pgdp.colony.Genome");
    static final DynamicConstructor<?> GenomeRandom = Genome.constructor();
    static final DynamicConstructor<?> GenomeCustom = Genome.constructor(String.class);
    static final DynamicMethod<Integer> lifespan = Genome.method(int.class, "lifespan");
    static final DynamicMethod<Integer> maxSize = Genome.method(int.class, "maxSize");
    static final DynamicMethod<Integer> skill = Genome.method(int.class, "skill");
    static final DynamicMethod<Byte> color = Genome.method(byte.class, "color");
    static final DynamicMethod<Boolean> isMale = Genome.method(boolean.class, "isMale");

    //Penguin
    static final DynamicClass<?> Penguin = DynamicClass.toDynamic("pgdp.colony.Penguin");
    static final DynamicMethod<String> getName = Penguin.method(String.class, "getName");
    static final DynamicMethod<Genome> getGenome = Penguin.method(Genome.class, "getGenome");
    static final DynamicMethod<Integer> getAge = Penguin.method(int.class, "getAge");
    static final DynamicMethod<Integer> getSize = Penguin.method(int.class, "getSize");
    static final DynamicMethod<Integer> getHealth = Penguin.method(int.class, "getHealth");
    static final DynamicMethod<Integer> getNumFish = Penguin.method(int.class, "getNumFish");
    static final DynamicMethod<Penguin> getChild = Penguin.method(Penguin.class, "getChild");
    static final DynamicMethod<String> toString = Penguin.method(String.class, "toString");

    //PenguinColony
    static final DynamicClass<?> PenguinColony = DynamicClass.toDynamic("pgdp.colony.PenguinColony");
    static final DynamicConstructor<?> PenguinColonyConstr = PenguinColony.constructor();
    static final DynamicMethod<Penguin> createPenguin = PenguinColony.method(Penguin.class, "createPenguin", Genome.class);
    static final DynamicMethod<PenguinList> getPopulation = PenguinColony.method(PenguinList.class, "getPopulation");
    static final DynamicMethod<Integer> getTime = PenguinColony.method(int.class, "getTime");
    static final DynamicMethod<Void> simulateDay = PenguinColony.method(void.class, "simulateDay");

    static boolean isComplete() {
        return Genome.exists()
                && GenomeRandom.isCallable()
                && GenomeCustom.isCallable()
                && lifespan.isCallable()
                && maxSize.isCallable()
                && skill.isCallable()
                && color.isCallable()
                && isMale.isCallable()
                && Penguin.exists()
                && getName.isCallable()
                && getGenome.isCallable()
                && getAge.isCallable()
                && getSize.isCallable()
                && getHealth.isCallable()
                && getNumFish.isCallable()
                && getChild.isCallable()
                && toString.isCallable()
                && PenguinColony.exists()
                && PenguinColonyConstr.isCallable()
                && createPenguin.isCallable()
                && getPopulation.isCallable()
                && getTime.isCallable()
                && simulateDay.isCallable();
    }

    static Genome newGenome() {
        return (Genome) GenomeRandom.newInstance();
    }

    static Genome newGenome(String dna) {
        return (Genome) GenomeCustom.newInstance(dna);
    }

    static PenguinColony newPenguinColony() {
        return (PenguinColony) PenguinColonyConstr.newInstance();
    }
}