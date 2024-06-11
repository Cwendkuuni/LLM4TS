package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;
import java.util.ChartDataModel;
import java.util.ChartDataModelConstraints;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.model.DataColumn;
import de.progra.charting.model.DataSeries;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities utilities;
    private CoordSystem coordSystem;
    private ChartDataModel model;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;

    @Before
    public void setup() {
        constraints = new ChartDataModelConstraints();
        constraints2 = new ChartDataModelConstraints();
        model = new ChartDataModel();
        coordSystem = new CoordSystem(100, 100, 500, 500);
        utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
    }

    // Test cases for all methods here

}