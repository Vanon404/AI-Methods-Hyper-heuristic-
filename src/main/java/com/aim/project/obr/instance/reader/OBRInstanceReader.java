package com.aim.project.obr.instance.reader;

import java.nio.file.Path;
import java.util.Random;

import com.aim.project.obr.interfaces.OBRInstanceInterface;
import com.aim.project.obr.interfaces.OBRInstanceReaderInterface;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;

import com.aim.project.obr.instance.Location;
import com.aim.project.obr.instance.OpenTopBusRoutingInstance;
import com.aim.project.obr.interfaces.OBRInstanceInterface;
import com.aim.project.obr.interfaces.OBRInstanceReaderInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (15/03/2026)
 */
public class OBRInstanceReader implements OBRInstanceReaderInterface {

	@Override
	public OBRInstanceInterface readOBRInstanceFile(Path oPath, Random oRandom) {

        Location oBusDepot = null;
        ArrayList<Location> oPois = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(oPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.equals("BUS_DEPOT_LOCATION")) {
                    // The next line contains the X and Y
                    String[] coords = reader.readLine().trim().split("\\s+");
                    oBusDepot = new Location(-1, Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));

                } else if (line.equals("POINTS_OF_INTEREST")) {
                    // Read until we hit EOF
                    int id = 0;
                    while (!(line = reader.readLine().trim()).equals("EOF")) {
                        if (line.isEmpty()) continue;
                        String[] coords = line.split("\\s+");
                        oPois.add(new Location(id++, Integer.parseInt(coords[0]), Integer.parseInt(coords[1])));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert the ArrayList to a standard Array for the constructor
        Location[] aoPois = oPois.toArray(new Location[0]);

        // Return the new instance
        return new OpenTopBusRoutingInstance(aoPois.length, aoPois, oBusDepot, oRandom);
    }
}