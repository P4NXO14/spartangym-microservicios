package com.spartangym.pagos.controller;

import com.spartangym.pagos.dto.PagoPlanDTO;
import com.spartangym.pagos.dto.PagoProductoDTO;
import com.spartangym.pagos.model.Pago;
import com.spartangym.pagos.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Registro y control de pagos de planes y productos del gimnasio")
public class PagoController {

    private final PagoService pagoService;

    @Operation(
            summary = "Listar pagos",
            description = "Obtiene la lista con todos los pagos registrados en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Pago>> listar() {
        return ResponseEntity.ok(pagoService.listarTodos());
    }

    @Operation(
            summary = "Buscar pago por ID",
            description = "Obtiene los datos de un pago especifico a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pagoService.buscarPorId(id));
    }

    @Operation(
            summary = "Listar pagos por cliente",
            description = "Obtiene todos los pagos realizados por un cliente especifico"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Pago>> listarPorCliente(@PathVariable Integer idCliente) {
        return ResponseEntity.ok(pagoService.listarPorCliente(idCliente));
    }

    @Operation(
            summary = "Verificar plan activo",
            description = "Indica si un cliente tiene actualmente un plan de membresia activo"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificacion realizada correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/cliente/{idCliente}/plan-activo")
    public ResponseEntity<Boolean> clienteTienePlanActivo(@PathVariable Integer idCliente) {
        return ResponseEntity.ok(pagoService.clienteTienePlanActivo(idCliente));
    }

    @Operation(
            summary = "Listar pagos por tipo",
            description = "Obtiene los pagos filtrados por tipo (PLAN o PRODUCTO)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Tipo de pago no valido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/tipo/{tipoPago}")
    public ResponseEntity<List<Pago>> listarPorTipo(@PathVariable String tipoPago) {
        return ResponseEntity.ok(pagoService.listarPorTipo(tipoPago));
    }

    @Operation(
            summary = "Registrar pago de plan",
            description = "Permite registrar el pago de un plan de membresia para un cliente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pago de plan registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del pago invalidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/plan")
    public ResponseEntity<Pago> registrarPagoPlan(@Valid @RequestBody PagoPlanDTO pagoPlanDTO) {
        Pago pago = pagoService.registrarPagoPlan(pagoPlanDTO);
        return ResponseEntity.status(201).body(pago);
    }

    @Operation(
            summary = "Registrar pago de producto",
            description = "Permite registrar el pago de un producto comprado por un cliente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pago de producto registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del pago invalidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/producto")
    public ResponseEntity<Pago> registrarPagoProducto(@Valid @RequestBody PagoProductoDTO pagoProductoDTO) {
        Pago pago = pagoService.registrarPagoProducto(pagoProductoDTO);
        return ResponseEntity.status(201).body(pago);
    }

    @Operation(
            summary = "Cancelar pago",
            description = "Permite cancelar un pago previamente registrado a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago cancelado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Pago> cancelar(@PathVariable Integer id) {
        return ResponseEntity.ok(pagoService.cancelarPago(id));
    }

    @Operation(
            summary = "Cancelar pago por tipo y referencia",
            description = "Permite cancelar un pago a partir de su tipo (PLAN o PRODUCTO) y el ID de referencia asociado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago cancelado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/cancelar/tipo/{tipoPago}/referencia/{referenciaId}")
    public ResponseEntity<Pago> cancelarPorTipoYReferencia(
            @PathVariable String tipoPago,
            @PathVariable Integer referenciaId) {

        return ResponseEntity.ok(pagoService.cancelarPorTipoYReferencia(tipoPago, referenciaId));
    }

    @Operation(
            summary = "Eliminar pago",
            description = "Elimina un pago a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pago eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}