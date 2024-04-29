package com.example.ronys.RestController;

import com.example.ronys.Model.*;
import com.example.ronys.Repository.EmployeeRepository;
import com.example.ronys.Repository.ProductDetailsRepository;
import com.example.ronys.Repository.ProductRepository;
import com.example.ronys.Repository.SellItemRepository;
import com.example.ronys.Services.ProductService;
import com.example.ronys.Services.SellItemService;
import com.example.ronys.Services.TransactionService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController

public class BarcodeController {
    private final ProductService productService;
    private final TransactionService transactionService;
    private final SellItemService sellItemService;
    private final ProductRepository productRepository;
    private final ProductDetailsRepository productDetailsRepository;
    private final SellItemRepository sellItemRepository;
    private final EmployeeRepository employeeRepository;


    @Autowired
    public BarcodeController(ProductService productService, TransactionService transactionService, SellItemService sellItemService, ProductRepository productRepository, ProductDetailsRepository productDetailsRepository, SellItemRepository sellItemRepository, EmployeeRepository employeeRepository) {
        this.productService = productService;
        this.transactionService = transactionService;
        this.sellItemService = sellItemService;
        this.productRepository = productRepository;
        this.productDetailsRepository = productDetailsRepository;
        this.sellItemRepository = sellItemRepository;
        this.employeeRepository = employeeRepository;
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/generateBarcode")
    public ResponseEntity<byte[]> generateBarcode(@RequestBody BarCode barCode) {
        // Generate barcode image based on product ID
        System.out.println("called");
        System.out.println(barCode.getProductId());
        System.out.println(barCode.getSize());
        try {
            Optional<Product> product = productRepository.findById(barCode.getProductId());
            String productId = String.valueOf(barCode.getProductId());
            String size = String.valueOf(barCode.getSize());
            String price = String.valueOf(product.get().getSellingPrice());
            String colorCode = ""; // Initialize color code variable

            if (barCode.getColor().equals("Black")) {
                colorCode = "1"; // Set color code to "1" if color is Black
            } else if (barCode.getColor().equals("Chocolate")) {
                colorCode = "2"; // Set color code to "2" if color is Chocolate
            }

            String id = productId + size + colorCode;
            BufferedImage barcodeImage = generateBarcodeImage(id, size, price, barCode.getColor());

            // Convert BufferedImage to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(barcodeImage, "png", baos);
            byte[] barcodeBytes = baos.toByteArray();

            // Return the barcode image as byte array in the response
            System.out.println("success");
            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.IMAGE_PNG)
                    .body(barcodeBytes);
        } catch (IOException | WriterException e) {
            // Handle exception
            System.out.println("sending null");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private BufferedImage generateBarcodeImage(String productId, String size, String price, String color) throws WriterException {
        // Adjust barcode dimensions (optional)
        int barcodeWidth = 200;  // Adjust as needed
        int barcodeHeight = 50;   // Adjust as needed

        // Create the barcode writer
        Code128Writer writer = new Code128Writer();

        // Encode the product ID as barcode
        BitMatrix bitMatrix = writer.encode(productId, BarcodeFormat.CODE_128, barcodeWidth, barcodeHeight);

        // Calculate image dimensions based on barcode size and desired margins
        int imageMargin = 20;  // Adjust margins for spacing around content
        int imageWidth = barcodeWidth + 2 * imageMargin;
        int imageHeight = barcodeHeight + 80;  // Height for barcode, labels, and spacing

        // Create BufferedImage with white background
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, imageWidth, imageHeight);

        // Draw the barcode
        g2d.setColor(Color.BLACK);
        for (int x = 0; x < barcodeWidth; x++) {
            for (int y = 0; y < barcodeHeight; y++) {
                if (bitMatrix.get(x, y)) {
                    g2d.fillRect(x + imageMargin, y + imageMargin, 1, 1);
                }
            }
        }

        // Draw the product ID above the barcode (optional)
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.setColor(Color.BLACK);
        int textWidth = g2d.getFontMetrics().stringWidth(productId);
        g2d.drawString("Ronys Shoes: " + productId, 55, imageMargin - 5);  // Centered alignment

        // Draw labels with centering and formatting
        int labelYOffset = imageMargin + barcodeHeight + 20;  // Spacing between barcode and labels
        g2d.setFont(new Font("Arial", Font.BOLD, 14));  // Bold font for size

        // Size label
        textWidth = g2d.getFontMetrics().stringWidth("Size: " + size);
        int labelXOffset = (imageWidth - textWidth) / 2;  // Centered alignment
        g2d.drawString("Size: " + size+ "\n    Color: "+ color, 55, 90);

        // Price label with larger font and bold
        g2d.setFont(new Font("Arial", Font.BOLD, 20));  // Larger and bold font for price
        String formattedPrice = "Price: " + price;
        textWidth = g2d.getFontMetrics().stringWidth(formattedPrice);
        labelXOffset = (imageWidth - textWidth) / 2;  // Centered alignment
        g2d.drawString(formattedPrice, labelXOffset, labelYOffset + 25); // Adjusted Y-offset for separate line

        g2d.dispose();

        return image;
    }
    
@CrossOrigin(origins = "http://localhost:3000")
@Transactional
@PutMapping("/sellItems")
public ResponseEntity<Invoice> sellItems(@RequestBody List<SellItem> sellItems) {
    System.out.println("called");

    Transaction transaction = createTransaction();

    Invoice invoice = new Invoice();
    invoice.setShopName("Ronys Shoes");
    invoice.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

    List<InvoiceItem> invoiceItems = new ArrayList<>();
    int sum = 0;
    int totalProfit = 0;
    for (SellItem sellItem : sellItems) {
        try {
            String id = String.valueOf(sellItem.getBarcode());

            String productIdStr = id.substring(0, id.length() - 3); // Get all characters except the last 3
            Long productId = Long.valueOf(productIdStr);

            String sizeStr = id.substring(id.length() - 3, id.length() - 1); // Get the second-to-last character
            int size = Integer.parseInt(sizeStr);

            String colorCodeStr = id.substring(id.length() - 1); // Get the last two characters
            int colorCode = Integer.parseInt(colorCodeStr);
            String color = "";
            if(colorCode == 1)
                color = "Black";
            else if (colorCode == 2) {
                color = "Chocolate";
            }
            sellItem.setSize(size);
            sellItem.setColor(color);
            sellItem.setBarcode(productId);

            ProductDetails productDetails = productDetailsRepository.findByProductIdAndSize(productId, size, color);
            Employee employee = this.employeeRepository.findByName(sellItem.getSeller());
            sellItem.setEmployee(employee);

            if (productDetails.getQuantity() < 1) {
                throw new RuntimeException("Product with ID " + sellItem.getBarcode() + " is out of stock.");
            }

            productDetails.setQuantity(productDetails.getQuantity() - 1);
            productDetailsRepository.save(productDetails); // Persist product quantity update

            sellItem.setTransaction(transaction);
            sellItem.setProductDetails(productDetails);

            Optional<Product> product = this.productRepository.findById(sellItem.getBarcode());

            sum += product.get().getSellingPrice() - sellItem.getDiscount();
            totalProfit += product.get().getSellingPrice() - product.get().getBuyingPrice() - sellItem.getDiscount();

            InvoiceItem item = new InvoiceItem();
            item.setProductId(productId);
            item.setSize(sellItem.getSize());
            item.setQuantity(1); // Assuming quantity is always 1 for sold items
            item.setDiscount(sellItem.getDiscount());
            item.setPrice(product.get().getSellingPrice() - sellItem.getDiscount());
            item.setSeller(sellItem.getSeller());
            item.setColor(sellItem.getColor());
            invoiceItems.add(item);

        } catch (Exception e) {
            // Log the error and rollback the transaction

            throw new RuntimeException("Failed to sell items.", e); // Trigger transaction rollback
        }
    }

    invoice.setItems(invoiceItems);
    invoice.setSubTotal(sum);
    invoice.setTotalPrice(sum);

    if (!invoiceItems.isEmpty()) {
        transaction.setTotalPrice(sum);
        transaction.setTotalProfit(totalProfit);
        transactionService.addTransaction(transaction); // Assuming method exists
        sellItemRepository.saveAll(sellItems); // Persist sell items
    }

    return ResponseEntity.ok(invoice);
}

    // Your existing method to create a transaction
    private Transaction createTransaction() {
        Transaction transaction = new Transaction();
        transaction.setDate(LocalDateTime.now());
        return transaction;
    }

}
