package com.andrei.DataAccess;


import com.andrei.BusinessLogic.BaseProduct;
import com.andrei.BusinessLogic.DeliveryService;
import com.andrei.BusinessLogic.MenuItem;
import com.andrei.BusinessLogic.Order;
import com.andrei.Controller.EmployeeController;
import com.andrei.Controller.ReportController.Report3Controller;
import com.andrei.Controller.ReportController.Report4Controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

/**
 * This class contains methods for writing reports to a file and generating bills for orders.
 */
public class ReportBillWriter {

    /**
     * This method generates a bill for a given order. The file name is dynamically generated using
     * the date in the specified format on the first line and the client name. Details included in the
     * report are the date, client details, ordered products and the total value of the order.
     * @param order -> order with info to be permeated to a file. A call to order details is necessary in order
     *              to get the menu items associated with the order.
     */
    public void generateBill(Order order)
    {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd---HH-mm-ss");
            String formattedDate = df.format(order.getDate());
            FileWriter myWriter = new FileWriter("orderBills\\order_"+order.getClient().getUsername()+"_"+formattedDate+".txt");
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.write("BILL for order with on date " + order.getDate() +"\n");
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.write("Client:"+ order.getClient().getUsername()+
                    "\nphone number:" + order.getClient().getPhoneNumber() + "\n");
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.write("Ordered products are:\n");
            for (MenuItem menuItem: DeliveryService.getInstance().getOrderDetails().get(order))
            {
                myWriter.write(menuItem.getTitle() + "\n");
            }
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.write("Total order price is: " + order.getPrice()+"\n"
            );
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.close();
        } catch (IOException e) {
            System.err.println("An error has occurred while writing to a file!");
        }
    }

    /**
     * Deletes the file associated with an order that no longer exists. If the file
     * cannot be found or the deletion otherwise did not happen, an alert will be printed
     * to system error.
     * @param order -> order object to have its corresponding bill deleted.
     */
    public void deleteBill(Order order)
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd---HH-mm-ss");
        String formattedDate = df.format(order.getDate());
        File myObj = new File("orderBills\\order_"+order.getClient().getUsername()+"_"+formattedDate+".txt");
        if (!myObj.delete()) {
            System.err.println("Failed to delete the file!");
        }
    }

    /**
     * Writes the details of report 1 to a file "contains all the orders completed between different times of the day"
     * Prints to system error will be made if writing cannot happen.
     * @param updatedList -> list to write
     * @param minSecondFilter -> filter, -1 if not applied
     * @param maxSecondFilter -> filter, -1 if not applied
     * @param minMinuteFilter -> filter, -1 if not applied
     * @param maxMinuteFilter -> filter, -1 if not applied
     * @param minHourFilter -> filter, -1 if not applied
     * @param maxHourFilter -> filter, -1 if not applied
     */
    public void writeReport1(List<EmployeeController.FullOrder> updatedList,
                             int minSecondFilter,int maxSecondFilter,
                             int minMinuteFilter,int maxMinuteFilter,
                             int minHourFilter, int maxHourFilter)
    {
        try {
            FileWriter myWriter = new FileWriter("reports\\report1.txt");
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.write("REPORT 1 for ADMINISTRATOR\n");
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.write(("This report contains all the orders completed between different times of the day.\n"));
            myWriter.write(("Filters applied:\n"));
            if(minSecondFilter != -1) myWriter.write("minimum seconds: " + minSecondFilter + ";\n");
            if(maxSecondFilter != -1) myWriter.write("maximum seconds: " + maxSecondFilter + ";\n");
            if(minMinuteFilter != -1) myWriter.write("minimum minutes: " + minMinuteFilter + ";\n");
            if(maxMinuteFilter != -1) myWriter.write("maximum minutes: " + maxMinuteFilter + ";\n");
            if(minHourFilter != -1) myWriter.write("minimum hour: " + minHourFilter + ";\n");
            if(minHourFilter != -1) myWriter.write("maximum hour: " + maxHourFilter + ";\n");
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.write(("Orders that fit the criteria (empty means no orders fit criteria):\n"));
            updatedList.forEach(
                    fullOrder -> {
                        try {
                            myWriter.write("Client: "+fullOrder.getClientName() +"; "+
                           "Date:"+ fullOrder.getDate() +"; " +
                                    "Value: "+ fullOrder.getPrice() +";\n"+
                                    "Contents:\n"+ fullOrder.getContents());
                        } catch (IOException e) {
                            System.err.println("Error when writing to file!");
                        }
                    }
            );
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.close();

        } catch (IOException e) {
            System.err.println("An error has occurred while writing to a file!");
        }
    }

    /**
     * Generates report 2 for administrator: "contains all the products ordered a minimum number of times so far"
     * @param updatedList -> updated list of menu items to write to file
     * @param filter -> filter for orders, if -1 will be considered 0.
     */
    public void writeReport2(List<MenuItem> updatedList,
                             int filter)
    {
        try {
            FileWriter myWriter = new FileWriter("reports\\report2.txt");
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.write("REPORT 2 for ADMINISTRATOR\n");
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.write(("This report contains all the products ordered a minimum number of times so far.\n"));
            if(filter != -1) myWriter.write("minimum number of times ordered: " + filter + ";\n");
            else myWriter.write("minimum number of times ordered: 0;\n");
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.write(("Products that fit the criteria (empty means no products fit criteria):\n"));
            updatedList.forEach(
                    menuItem -> {
                        try {
                            if(menuItem instanceof BaseProduct)
                            {
                                myWriter.write("Product Title: "+menuItem.getTitle() +"; price:"
                                        + menuItem.computePrice() +";\n");
                            }
                            else
                            {
                                myWriter.write("Product Title: "+menuItem.getTitle() +"; price:"
                                        + menuItem.computePrice() +"; Contains:\n"+ menuItem.getContains()+"\n");
                            }

                        } catch (IOException e) {
                            System.err.println("Error when writing to file!");
                        }
                    }
            );
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.close();

        } catch (IOException e) {
            System.err.println("An error has occurred while writing to a file!");
        }
    }

    /**
     * Generates report 3 for administrator: "all the clients that have ordered a minimum number of times so far"
     *  and the order value higher than a specific amount
     * @param reportClients -> clients to write to file
     * @param minOrderFilter -> minimum number of orders for each client
     * @param minPriceFilter -> minimum price for each order
     */
    public void writeReport3(List<Report3Controller.ReportClient> reportClients ,int minOrderFilter, double minPriceFilter)
    {
        try {
            FileWriter myWriter = new FileWriter("reports\\report3.txt");
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.write("REPORT 3 for ADMINISTRATOR\n");
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.write(("This report contains all the clients that have ordered a minimum number of times so far\n and the order value higher than a specific amount.\n"));
            if(minOrderFilter != -1) myWriter.write("minimum number of times ordered: " + minOrderFilter + ";\n");
            if(minPriceFilter != -1) myWriter.write("minimum price for each order: " + minPriceFilter + ";\n");
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.write(("Clients that fit the criteria (empty means no clients fit criteria):\n"));
            reportClients.forEach(
                    reportClient -> {
                        try {
                            myWriter.write("Client name:" + reportClient.getUsername() +
                                    "; phone number:" + reportClient.getPhoneNumber()+
                                    "; number of orders matching filters: "+reportClient.getNoOfOrders() +";\n");
                        } catch (IOException e) {
                            System.err.println("Error when writing to file!");
                        }
                    }
            );
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.close();

        } catch (IOException e) {
            System.err.println("An error has occurred while writing to a file!");
        }
    }

    /**
     * Generates report 4 for administrator: all the products ordered within a specific day and the number of times they have been ordered.
     * @param reportProducts -> products that have been filtered
     * @param localDate -> filter date for report
     */
    public void writeReport4(List<Report4Controller.ReportProduct> reportProducts , LocalDate localDate)
    {
        try {
            FileWriter myWriter = new FileWriter("reports\\report4.txt");
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.write("REPORT 4 for ADMINISTRATOR\n");
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.write(("This report contains all the products ordered within a specific day and the number of times they have been ordered.\n"));
            if(localDate != null) myWriter.write("Date requested for report: " + localDate + ";\n");
            else  myWriter.write("No date was requested for the report, counting all days!\n");
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.write(("Products that fit the criteria (empty means no products fit criteria):\n"));
            reportProducts.forEach(
                    reportProduct -> {
                        try {
                            myWriter.write("Product title: " + reportProduct.getTitle() +"; price:" +
                                    reportProduct.getPrice() + "; number of times ordered: " + reportProduct.getNoOfTimesOrdered() + "\n");
                        } catch (IOException e) {
                            System.err.println("Error when writing to file!");
                        }
                    }
            );
            myWriter.write(("-------------------------------------------------\n"));
            myWriter.close();
        } catch (IOException e) {
            System.err.println("An error has occurred while writing to a file!");
        }
    }
}
