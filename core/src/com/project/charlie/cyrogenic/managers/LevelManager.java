package com.project.charlie.cyrogenic.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.World;
import com.project.charlie.cyrogenic.handlers.StageHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Charlie on 24/02/2016.
 */
public class LevelManager {

    static World world;

    public LevelManager() {
    }

    public static StageHandler parseLevel(World gameWorld) {
        world = gameWorld;
        StageHandler stage = new StageHandler();
        int turretCount = 0; // todo: read in further XML info

        try {

            FileHandle fileHandle = Gdx.files.internal("levels/level.xml"); // todo dynamic level loading

            Gdx.app.log("XML", "Parsing.." + Gdx.files.getLocalStoragePath());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document level = builder.parse(fileHandle.read());
            Gdx.app.log("XML", "parsed");

            level.getDocumentElement().normalize(); // recommended but optional apparently

            Gdx.app.log("XML", "Root element: " + level.getDocumentElement().getNodeName());

            NodeList nodeList = level.getElementsByTagName("stage");
            for (int s = 0; s < nodeList.getLength(); s++) {
                Node stageNode = nodeList.item(s);
                Gdx.app.log("XML", "Found stage node " + stageNode.getNodeName());
                if (stageNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element stageElement = (Element) stageNode;
                    stage.setId(Integer.parseInt(
                            stageElement.getAttribute("id")));
                    Gdx.app.log("XML", "Stage ID: " + stage.getId());

                    NodeList stageActorList = stageNode.getChildNodes();
                    for (int a = 0; a < stageActorList.getLength(); a++) {
                        Node actorNode = stageActorList.item(a);
                        if (actorNode.getNodeName().equals("asteroid")) {
                            NodeList asteroidNodes = actorNode.getChildNodes();
                            Gdx.app.log("XML", "N Of Nodes " + asteroidNodes.getLength());

                            for (int actor = 0; actor < asteroidNodes.getLength(); actor++) {
                                if (asteroidNodes.item(actor).getNodeType() == Node.ELEMENT_NODE) {
                                    Gdx.app.log("XML", "loop " + asteroidNodes.item(actor).getNodeName());
                                    Element asteroidElement = (Element) asteroidNodes.item(actor);
                                    if (asteroidElement.getNodeName().equals("number")) {
                                        stage.setAsteriodCount(Integer.parseInt(
                                                asteroidElement.getTextContent()));
                                    } else if (asteroidElement.getNodeName().equals("interval")) {
                                        stage.setAsteriodInterval(Float.parseFloat(
                                                asteroidElement.getTextContent()));
                                    } else if (asteroidElement.getNodeName().equals("speed")) {
                                        stage.setAsteriodSpeed(Float.parseFloat(
                                                asteroidElement.getTextContent()));
                                    }
                                }
                            }
                        } else if (actorNode.getNodeName().equals("turret")) {
                            float x = 0f;
                            float y = 0f;
                            float firerate = 0f;
                            NodeList turretNode = actorNode.getChildNodes();
                            for (int turret = 0; turret < turretNode.getLength(); turret++) {
                                if (turretNode.item(turret).getNodeType() == Node.ELEMENT_NODE) {
                                    Element turretElement = (Element) turretNode.item(turret);
                                    Gdx.app.log("XML", "Turret - " + turretElement.getNodeName());
                                    if (turretElement.getNodeName().equals("x")) {
                                        x = Float.parseFloat(turretElement.getTextContent());
                                    } else if (turretElement.getNodeName().equals("y")) {
                                        y = Float.parseFloat(turretElement.getTextContent());
                                    } else if (turretElement.getNodeName().equals("firerate")) {
                                        firerate = Float.parseFloat(turretElement.getTextContent());
                                        Gdx.app.log("XML", "Turret ATKSPD: " + firerate);
                                    }
                                }
                            }
                            StageHandler.TurretTemplate tempTurret = stage.new TurretTemplate(x, y, firerate);
                            stage.addTurret(tempTurret);
                            turretCount++;
                            Gdx.app.log("XML", "TCount: " + turretCount);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Gdx.app.log("XML", "Exception - " + e.getMessage());
            e.printStackTrace();
        }
        return stage;
    }


}