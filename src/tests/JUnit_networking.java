package tests;

import actions.Actions;
import enums.LinkTypes;
import enums.PacketTypes;
import exceptions.BadCallException;
import hardware.router.Standard2ETHRouter;
import hardware.switchs.Standard24Switch;
import org.junit.Test;
import packet.IP;
import packet.Packet;

import java.util.ArrayList;

public class JUnit_networking
{
    /**
     * A -- SRA -- central -- SRB -- B   (SR = switch)
     */
    @Test
    public void setUp()
    {
        try {
            Standard2ETHRouter central = new Standard2ETHRouter(new ArrayList<Integer>(){{add(1);add(2);}},
                new ArrayList<IP>(){{add(new IP(192,168,0,1));add(new IP(192,168,1,1));}},
                new ArrayList<IP>(){{add(new IP(255,255,255,0));add(new IP(255,255,255,0));}});

            Standard2ETHRouter A = new Standard2ETHRouter(new ArrayList<Integer>(){{add(3);add(4);}},
                new ArrayList<IP>(){{add(new IP(192,168,0,2));add(new IP(192,168,3,1));}},
                new ArrayList<IP>(){{add(new IP(255,255,255,0));add(new IP(255,255,255,0));}});

            Standard2ETHRouter B = new Standard2ETHRouter(new ArrayList<Integer>(){{add(5);add(6);}},
                new ArrayList<IP>(){{add(new IP(192,168,1,2));add(new IP(192,168,4,1));}},
                new ArrayList<IP>(){{add(new IP(255,255,255,0));add(new IP(255,255,255,0));}});

            Standard24Switch srA = new Standard24Switch();
            Standard24Switch srB = new Standard24Switch();


            Actions.connect(A, srA, LinkTypes.ETH);
            Actions.connect(B, srB, LinkTypes.ETH);
            Actions.connect(central, srA, LinkTypes.ETH);
            Actions.connect(central, srB, LinkTypes.ETH);

            Packet p =new Packet(new IP(192,168,1,2), new IP(255,255,255,0),
                    new IP(192,168,0,2), new IP(255,255,255,0),
                    3, 1, PacketTypes.WEB, false, true); // Ce paquet a un bon NHR et une bonne mac cible (il a fait ARP)
            p.setNHR(new IP(192,168,0,1));
            A.send(p, 0);
        } catch (BadCallException e) {
            e.printStackTrace();
        }
    }


}