package com.dronerecon.ws;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

/**
 *
 * @author Melissa :-)
 */
public class DroneDataService extends HttpServlet {


    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=utf-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        PrintWriter out = response.getWriter();

        // ##############################
        // 1. Get params passed in.

        // Get the following parameters from the request object and put them into strings:
        // area_id
        // tilex
        // tiley
        // totalcols
        // totalrows
        // ##############################

        String sAreaID = request.getParameter("area_id");
        String sTileX = request.getParameter("tilex");
        String sTileY = request.getParameter("tiley");
        String sTotalCols = request.getParameter("totalcols");
        String sTotalRows = request.getParameter("totalrows");

        //PortalDBService
        String sR = request.getParameter("r");
        String sG = request.getParameter("g");

        //Convert to integers
        int iTotalCols = Integer.parseInt(sTotalCols);
        int iTotalRows = Integer.parseInt(sTotalRows);
        int iTileX = Integer.parseInt(sTileX);
        int iTileY = Integer.parseInt(sTileY);

        try {

            // Call PortalDBService
            URL url = new URL("http://127.0.0.1:8080/dronereconportal/PortalDBService?area_id=" + sAreaID + "&tilex=" + sTileX + "&tiley=" + sTileY + "&r=" + sR + "&g=" + sG +"");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.toString());
        }


        // ##############################
        // 2. Default value of beginning direction.

        // Set a string called sDirection to "right".
        // ##############################
        String sDirection = "right";



        // ##############################
        // 3. Calculate next drone move.

        // Determine next tile to move to.
        // Base this on current x and y.
        // Change sDirection if necessary.
        // Drone must serpentine from top left of grid back and forth down.
        // If rows are done, change sDirection to "stop".
        // ##############################

        //Check if on even row
        if(iTileY % 2 == 0) {
           //Check if drone on last column, then increase y
           if(iTileX == iTotalCols - 1) {
              iTileY++;
              sDirection = "left";
           }
           //Drone is not on last column, so adjust x
           else {
              //increase x by 1
               iTileX++;
              //set direction to right
               sDirection = "right";
           }
        }
        //It's on odd row
        else {
            //Check if drone on far left column, then increase y
            if (iTileX == 0) {
                iTileY++;
                sDirection = "right";
            }
            //Drone is not on first column, so adjust x
            else {
                //Decrease x by 1
                iTileX--;
                sDirection = "left";
            }
        }
            //Check if drone is off grid and stop
            if (iTileY == iTotalRows) {
                sDirection = "stop";
            }

        // ##############################
        // 4. Format & Return JSON string to caller.

        /* Return via out.println() a JSON string like this:
        {"area_id":"[area id from above]", "nextTileX":"[next tile x]", "nextTileY":"[next tile y]", "direction":"[direction string from above]"}
        */
        // ##############################
        String sReturnJson = "{\"area_id\":\"" + sAreaID + "\", \"nextTileX\":\"" + sTileX + "\", \"nextTileY\":\"" + sTileY + "\", \"direction\":\"" + sDirection + "\"}";
        out.println(sReturnJson);
    }
}

