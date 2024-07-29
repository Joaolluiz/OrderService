package br.com.orderservice.controller;

import br.com.orderservice.dto.ProductDTO;
import br.com.orderservice.service.ProductService;
import br.com.orderservice.service.ReportService;
import br.com.orderservice.util.CustomMediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import java.io.ByteArrayInputStream;
import java.net.URI;

@RestController
@RequestMapping("api/product")
@Tag(name = "Product", description = "Endpoints for managing Product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    ReportService reportService;

    @GetMapping(produces = CustomMediaType.APPLICATION_JSON)
    @Operation(summary = "Lists all products", description = "Lists all products",
            tags = {"Product"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public Page<ProductDTO> listAllProducts(@PageableDefault(size = 10)Pageable pageable) {
        return productService.findAllProducts(pageable);
    }

    @GetMapping("/report")
    @Operation(summary = "Generate a report of products", description = "Generate a report of products",
    tags = {"Product"},
    responses = {
            @ApiResponse(description = "PDF report generated", responseCode = "200",
                    content = @Content(mediaType = "application/pdf")),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<InputStreamResource> getProductReport() {
        try {
            ByteArrayInputStream bis = reportService.generateProductReport();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=productReport.pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(CustomMediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping(value = "/{id}",
            produces = CustomMediaType.APPLICATION_JSON)
    @Operation(summary = "Finds a product by id", description = "Finds a product by id",
            tags = {"Product"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ProductDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ProductDTO findProductById(@PathVariable(value = "id") Long id) {
        return productService.findProductById(id);
    }

    @PostMapping(
            consumes = CustomMediaType.APPLICATION_JSON,
            produces = CustomMediaType.APPLICATION_JSON)
    @Operation(summary = "Adds a new Product",
            description = "Adds a new Product by passing in a JSON representation of the Product!",
            tags = {"Product"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ProductDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO product, UriComponentsBuilder uriBuilder) {

        ProductDTO productDTO = productService.saveProduct(product);
        URI address = uriBuilder.path("api/product/{id}").buildAndExpand(product.getId()).toUri();

        return ResponseEntity.created(address).body(productDTO);
    }

    @PutMapping(value = "/{id}",
            consumes = CustomMediaType.APPLICATION_JSON,
            produces = CustomMediaType.APPLICATION_JSON)
    @Operation(summary = "Updates a Product",
            description = "Updates a Product by passing in a JSON representation of the product!",
            tags = {"Product"},
            responses = {
                    @ApiResponse(description = "Updated", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ProductDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO product, @PathVariable(value = "id") Long id) {
        ProductDTO updated = productService.updateProduct(id, product);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a Product",
            description = "Deletes an id",
            tags = {"Product"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable(value = "id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
