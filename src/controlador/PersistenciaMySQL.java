package controlador;

import Entidades.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de PersistenciaHotel usando MySQL. Reemplaza a
 * PersistenciaArchivos. Coloca este archivo en src/controlador/
 */
public class PersistenciaMySQL implements PersistenciaHotel {

    private final Connection con;

    public PersistenciaMySQL() {
        this.con = ConexionDB.getConexion();
    }

    // =========================================================
    // guardarDatos — persiste TODO el estado del Hotel en MySQL
    // =========================================================
    @Override
    public void guardarDatos(Hotel hotel) {
        guardarEmpleados(hotel.getListaEmpleados());
        guardarHuespedes(hotel.getListaHuespedes());
        guardarHabitaciones(hotel.getListaHabitacion());
        guardarFichas(hotel.getListaFHospedaje());
        guardarProductos(hotel.getInventario());
        guardarOrdenes(hotel.getListaOrdenes());
        guardarTurnos(hotel.getListaTurnos());
        guardarPreferencias(hotel.getListaPreferencias()); // F-004 / F-011
        guardarPagosServicio(hotel.getListaPagosServicio()); // F-010
        System.out.println("Datos guardados en MySQL correctamente.");
    }

    // =========================================================
    // cargarDatos — reconstruye el objeto Hotel desde MySQL
    // =========================================================
    @Override
    public Hotel cargarDatos() {
        Hotel hotel = new Hotel();
        hotel.setListaEmpleados(cargarEmpleados());
        hotel.setListaHuespedes(cargarHuespedes());
        hotel.setListaHabitacion(cargarHabitaciones());
        hotel.setListaFHospedaje(cargarFichas(hotel));
        hotel.setListaPreferencias(cargarPreferencias());   // F-004 / F-011
        hotel.setListaPagosServicio(cargarPagosServicio()); // F-010
        hotel.setInventario(cargarProductos());             // Inventario / Almacén
        hotel.getListaOrdenes().addAll(cargarOrdenes());    // Órdenes de compra (bandeja + egresos dashboard)
        hotel.getListaTurnos().addAll(cargarTurnos());      // Turnos de caja (ingresos dashboard)
        System.out.println("Datos cargados desde MySQL correctamente.");
        return hotel;
    }

    // ---------------------------------------------------------
    // EMPLEADOS
    // ---------------------------------------------------------
    private void guardarEmpleados(List<Empleado> lista) {
        String sql = "INSERT INTO empleado (id, id_hotel, rol, sueldo, correo, "
                + "inicio_contrato, fin_contrato, nombre, apellido, tipo_documento, "
                + "num_documento, telefono, direccion) VALUES (?,1,?,?,?,?,?,?,?,?,?,?,?) "
                + "ON DUPLICATE KEY UPDATE rol=VALUES(rol), sueldo=VALUES(sueldo), "
                + "correo=VALUES(correo), fin_contrato=VALUES(fin_contrato)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (Empleado e : lista) {
                ps.setInt(1, e.getId());
                ps.setString(2, e.getRol());
                ps.setDouble(3, e.getSueldo());
                ps.setString(4, e.getCorreo());
                ps.setDate(5, new java.sql.Date(e.getInicioContrato().getTime()));
                ps.setDate(6, e.getFinContrato() != null ? new java.sql.Date(e.getFinContrato().getTime()) : null);
                ps.setString(7, e.getNombre());
                ps.setString(8, e.getApellido());
                ps.setString(9, e.getTipoDocumento());
                ps.setString(10, e.getNumDocumento());
                ps.setInt(11, e.getTelefono());
                ps.setString(12, e.getDireccion());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private List<Empleado> cargarEmpleados() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM empleado";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Empleado e = new Empleado(
                        rs.getInt("id"),
                        rs.getString("rol"),
                        rs.getDouble("sueldo"),
                        rs.getString("correo"),
                        rs.getDate("inicio_contrato"),
                        rs.getDate("fin_contrato"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("tipo_documento"),
                        rs.getString("num_documento"),
                        rs.getInt("telefono"),
                        rs.getString("direccion")
                );
                lista.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // ---------------------------------------------------------
    // HUÉSPEDES
    // ---------------------------------------------------------
    private void guardarHuespedes(List<Huesped> lista) {
        String sql = "INSERT INTO huesped (nombre, apellido, tipo_documento, num_documento, "
                + "telefono, direccion) VALUES (?,?,?,?,?,?) "
                + "ON DUPLICATE KEY UPDATE nombre=VALUES(nombre), direccion=VALUES(direccion)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (Huesped h : lista) {
                ps.setString(1, h.getNombre());
                ps.setString(2, h.getApellido());
                ps.setString(3, h.getTipoDocumento());
                ps.setString(4, h.getNumDocumento());
                ps.setInt(5, h.getTelefono());
                ps.setString(6, h.getDireccion());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private List<Huesped> cargarHuespedes() {
        List<Huesped> lista = new ArrayList<>();
        String sql = "SELECT * FROM huesped";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Huesped h = new Huesped(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("tipo_documento"),
                        rs.getString("num_documento"),
                        rs.getInt("telefono"),
                        rs.getString("direccion")
                );
                lista.add(h);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // ---------------------------------------------------------
    // HABITACIONES
    // ---------------------------------------------------------
    private void guardarHabitaciones(List<Habitacion> lista) {
        String sql = "INSERT INTO habitacion (numero, id_hotel, tipo, estado, precio, capacidad, "
                + "descripcion_problema, encargado_limpieza) VALUES (?,1,?,?,?,?,?,?) "
                + "ON DUPLICATE KEY UPDATE estado=VALUES(estado), precio=VALUES(precio), "
                + "descripcion_problema=VALUES(descripcion_problema), "
                + "encargado_limpieza=VALUES(encargado_limpieza)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (Habitacion h : lista) {
                ps.setString(1, h.getNumero());
                ps.setString(2, String.valueOf(h.getTipo()));
                ps.setString(3, String.valueOf(h.getEstado()));
                ps.setDouble(4, h.getPrecio());
                ps.setInt(5, 4);
                ps.setString(6, h.getDescripcionProblema());
                ps.setString(7, h.getEncargadoLimpieza());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private List<Habitacion> cargarHabitaciones() {
        List<Habitacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM habitacion";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Habitacion h = new Habitacion(
                        rs.getString("numero"),
                        rs.getString("tipo").charAt(0),
                        rs.getString("estado").charAt(0),
                        rs.getDouble("precio")
                );
                h.setDescripcionProblema(rs.getString("descripcion_problema"));
                h.setEncargadoLimpieza(rs.getString("encargado_limpieza"));
                lista.add(h);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // ---------------------------------------------------------
    // FICHAS DE HOSPEDAJE
    // ---------------------------------------------------------
    private void guardarFichas(List<FichaHospedaje> lista) {
        String sqlFicha = "INSERT INTO ficha_hospedaje (id_ficha, numero_habitacion, "
                + "id_huesped_titular, noches_esperadas, fecha_ingreso, fecha_salida, estado, "
                + "cantidad_personas, incluye_desayuno, incluye_almuerzo, incluye_cena, "
                + "estado_comida, arqueada) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) "
                + "ON DUPLICATE KEY UPDATE fecha_salida=VALUES(fecha_salida), "
                + "estado=VALUES(estado), arqueada=VALUES(arqueada)";

        String sqlLink = "INSERT IGNORE INTO ficha_huesped (id_ficha, id_huesped) "
                + "VALUES (?, (SELECT id FROM huesped WHERE num_documento = ?))";

        try (PreparedStatement psFicha = con.prepareStatement(sqlFicha); PreparedStatement psLink = con.prepareStatement(sqlLink)) {

            for (FichaHospedaje f : lista) {
                psFicha.setString(1, f.getIdFicha());
                psFicha.setString(2, f.getHabitacion().getNumero());
                // titular = primer huesped
                String docTitular = f.getHuespedes().get(0).getNumDocumento();
                psFicha.setString(3, "(SELECT id FROM huesped WHERE num_documento='" + docTitular + "')");
                // usamos subquery directo para el titular
                psFicha.setInt(3, obtenerIdHuesped(docTitular));
                psFicha.setInt(4, f.getNochesEsperadas());
                psFicha.setTimestamp(5, Timestamp.valueOf(f.getFechaIngreso()));
                psFicha.setTimestamp(6, f.getFechaSalida() != null ? Timestamp.valueOf(f.getFechaSalida()) : null);
                psFicha.setString(7, String.valueOf(f.getEstado()));
                psFicha.setInt(8, f.getCantidadPersonas());
                psFicha.setBoolean(9, f.isIncluyeDesayuno());
                psFicha.setBoolean(10, f.isIncluyeAlmuerzo());
                psFicha.setBoolean(11, f.isIncluyeCena());
                psFicha.setString(12, f.getEstadoComida());
                psFicha.setBoolean(13, f.isArqueada());
                psFicha.executeUpdate();

                // vincular todos los huéspedes de la ficha
                for (Huesped h : f.getHuespedes()) {
                    psLink.setString(1, f.getIdFicha());
                    psLink.setString(2, h.getNumDocumento());
                    psLink.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private int obtenerIdHuesped(String numDocumento) {
        String sql = "SELECT id FROM huesped WHERE num_documento = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, numDocumento);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    private List<FichaHospedaje> cargarFichas(Hotel hotel) {
        List<FichaHospedaje> lista = new ArrayList<>();
        String sql = "SELECT * FROM ficha_hospedaje";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String idFicha = rs.getString("id_ficha");
                String numHab = rs.getString("numero_habitacion");
                Habitacion hab = hotel.buscarHabitacionporNumero(numHab);

                List<Huesped> huespedes = cargarHuespedesDeFicha(idFicha, hotel);
                Timestamp tsIngreso = rs.getTimestamp("fecha_ingreso");
                LocalDateTime ingreso = tsIngreso.toLocalDateTime();

                FichaHospedaje f = new FichaHospedaje(
                        idFicha, huespedes, hab,
                        rs.getInt("noches_esperadas"), ingreso,
                        rs.getInt("cantidad_personas"),
                        rs.getBoolean("incluye_desayuno"),
                        rs.getBoolean("incluye_almuerzo"),
                        rs.getBoolean("incluye_cena")
                );
                f.setEstado(rs.getString("estado").charAt(0));
                f.setEstadoComida(rs.getString("estado_comida"));
                f.setArqueada(rs.getBoolean("arqueada"));

                Timestamp tsSalida = rs.getTimestamp("fecha_salida");
                if (tsSalida != null) {
                    f.setFechaSalida(tsSalida.toLocalDateTime());
                }

                lista.add(f);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    private List<Huesped> cargarHuespedesDeFicha(String idFicha, Hotel hotel) {
        List<Huesped> lista = new ArrayList<>();
        String sql = "SELECT h.num_documento FROM ficha_huesped fh "
                + "JOIN huesped h ON h.id = fh.id_huesped WHERE fh.id_ficha = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idFicha);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Huesped h = hotel.buscarHuespedPorDocumento(rs.getString("num_documento"));
                if (h != null) {
                    lista.add(h);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // ---------------------------------------------------------
    // PRODUCTOS
    // ---------------------------------------------------------
    private void guardarProductos(List<Producto> lista) {
        String sql = "INSERT INTO producto (id_hotel, nombre, tipo, stock, stock_minimo, "
                + "fecha_agregado) VALUES (1,?,?,?,?,?) "
                + "ON DUPLICATE KEY UPDATE stock=VALUES(stock), stock_minimo=VALUES(stock_minimo)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (Producto p : lista) {
                ps.setString(1, p.getNombre());
                ps.setString(2, p.getTipo());
                ps.setInt(3, p.getStock());
                ps.setInt(4, p.getStockMinimo());
                ps.setDate(5, new java.sql.Date(p.getFechaAgregado().getTime()));
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Reconstruye el inventario en memoria desde la tabla producto.
    private List<Producto> cargarProductos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT nombre, tipo, stock, stock_minimo, fecha_agregado FROM producto WHERE id_hotel = 1";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                java.util.Date fecha = rs.getDate("fecha_agregado");
                Producto p = new Producto(
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        rs.getInt("stock"),
                        fecha != null ? fecha : new java.util.Date(),
                        rs.getInt("stock_minimo"));
                lista.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // Registra un consumo de insumo (kardex) para trazabilidad de las salidas.
    // area: "LIMPIEZA" o "COCINA"; referencia: nro de habitación o id de pedido.
    public void registrarConsumo(String producto, int cantidad, String area,
            String referencia, String responsable) {
        String sql = "INSERT INTO consumo_insumo "
                + "(producto, cantidad, area, referencia, responsable, fecha) "
                + "VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, producto);
            ps.setInt(2, cantidad);
            ps.setString(3, area);
            ps.setString(4, referencia);
            ps.setString(5, responsable);
            ps.setTimestamp(6, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // ---------------------------------------------------------
    // ÓRDENES DE COMPRA
    // ---------------------------------------------------------
    private void guardarOrdenes(List<OrdenCompra> lista) {
        String sql = "INSERT INTO orden_compra (id_orden, id_producto, id_empleado, "
                + "cantidad, fecha_emision, fecha_entrega, proveedor, precio_total, estado) "
                + "VALUES (?,1,1,?,?,?,?,?,?) "
                + "ON DUPLICATE KEY UPDATE estado=VALUES(estado)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (OrdenCompra o : lista) {
                ps.setString(1, o.getIdOrden());
                ps.setInt(2, o.getCantidad());
                ps.setDate(3, new java.sql.Date(o.getFechaEmision().getTime()));
                ps.setDate(4, o.getFechaEntrega() != null ? new java.sql.Date(o.getFechaEntrega().getTime()) : null);
                ps.setString(5, o.getProveedor());
                ps.setDouble(6, o.getPrecioTotal());
                ps.setString(7, o.getEstado());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // ---------------------------------------------------------
    // TURNOS DE CAJA
    // ---------------------------------------------------------
    private void guardarTurnos(List<TurnoCaja> lista) {
        // Resolvemos un id de empleado REAL para el turno. El campo
        // empleadoTurno guarda el rol (texto), no un id, y la tabla
        // turno_caja exige una FK válida a empleado(id). Antes se usaba
        // id_empleado=1 fijo, que no existe -> el INSERT IGNORE fallaba en
        // silencio y los turnos nunca se guardaban. Tomamos el primer
        // recepcionista disponible; si no hay, cualquier empleado.
        int idEmpleadoCaja = resolverIdEmpleadoCaja();
        if (idEmpleadoCaja <= 0) {
            System.out.println("guardarTurnos: no hay empleados en la BD; no se guardan turnos.");
            return;
        }
        String sql = "INSERT IGNORE INTO turno_caja (id_empleado, total_sistema, "
                + "total_fisico, estado, fecha_cierre, motivo) VALUES (?,?,?,?,?,?)";
        // Evita duplicados: cada guardarCambios() re-recorre la lista en
        // memoria; sin este control, INSERT IGNORE (cuya PK es autoincrement)
        // insertaría de nuevo los turnos ya guardados e inflaría los ingresos.
        String existeSql = "SELECT COUNT(*) FROM turno_caja WHERE fecha_cierre=? AND total_fisico=?";
        try (PreparedStatement ps = con.prepareStatement(sql);
             PreparedStatement psExiste = con.prepareStatement(existeSql)) {
            for (TurnoCaja t : lista) {
                Timestamp fc = Timestamp.valueOf(t.getFechaCierre());
                psExiste.setTimestamp(1, fc);
                psExiste.setDouble(2, t.getTotalFisico());
                try (ResultSet rs = psExiste.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        continue; // ya está guardado, no duplicar
                    }
                }
                ps.setInt(1, idEmpleadoCaja);
                ps.setDouble(2, t.getTotalSistema());
                ps.setDouble(3, t.getTotalFisico());
                ps.setString(4, t.getEstado());
                ps.setTimestamp(5, fc);
                ps.setString(6, t.getMotivo() != null ? t.getMotivo() : "");
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /** Devuelve el id de un empleado válido para asociar a un cierre de caja.
     *  Prioriza un Recepcionista; si no hay, cualquier empleado. 0 si no existe. */
    private int resolverIdEmpleadoCaja() {
        String sql = "SELECT id FROM empleado "
                + "ORDER BY (rol LIKE '%ecepcion%') DESC, id ASC LIMIT 1";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException ex) { ex.printStackTrace(); }
        return 0;
    }

    // ---------------------------------------------------------
    // CARGA DE ÓRDENES DE COMPRA  (bandeja de revisión + egresos)
    // Une orden_compra con producto para recuperar nombre y tipo,
    // ya que la entidad OrdenCompra trabaja con nombreProducto/tipo.
    // ---------------------------------------------------------
    private List<OrdenCompra> cargarOrdenes() {
        List<OrdenCompra> lista = new ArrayList<>();
        String sql = "SELECT o.id_orden, o.cantidad, o.fecha_emision, o.fecha_entrega, "
                + "o.proveedor, o.precio_total, o.estado, "
                + "p.nombre AS producto, p.tipo AS tipo "
                + "FROM orden_compra o "
                + "LEFT JOIN producto p ON p.id = o.id_producto "
                + "ORDER BY o.fecha_emision";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                java.util.Date fEmision = rs.getDate("fecha_emision");
                java.util.Date fEntrega = rs.getDate("fecha_entrega");
                String nombreProd = rs.getString("producto");
                String tipoProd = rs.getString("tipo");
                OrdenCompra o = new OrdenCompra(
                        rs.getString("id_orden"),
                        nombreProd != null ? nombreProd : "(producto eliminado)",
                        tipoProd != null ? tipoProd : "",
                        rs.getInt("cantidad"),
                        fEmision,
                        fEntrega,
                        rs.getString("proveedor"),
                        rs.getDouble("precio_total"));
                o.setEstado(rs.getString("estado"));   // Pendiente / Aprobado / Rechazado
                lista.add(o);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // ---------------------------------------------------------
    // CARGA DE TURNOS DE CAJA  (ingresos del dashboard)
    // ---------------------------------------------------------
    private List<TurnoCaja> cargarTurnos() {
        List<TurnoCaja> lista = new ArrayList<>();
        String sql = "SELECT id_empleado, total_sistema, total_fisico, estado, "
                + "fecha_cierre, motivo FROM turno_caja ORDER BY fecha_cierre";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                // Reconstruimos el turno respetando los valores guardados
                // (no recalculamos el estado: usamos el persistido).
                TurnoCaja t = new TurnoCaja(
                        String.valueOf(rs.getInt("id_empleado")),
                        rs.getDouble("total_sistema"),
                        rs.getDouble("total_fisico"));
                java.sql.Timestamp ts = rs.getTimestamp("fecha_cierre");
                if (ts != null) {
                    t.setFechaCierre(ts.toLocalDateTime());
                }
                t.setEstado(rs.getString("estado"));
                t.setMotivo(rs.getString("motivo") != null ? rs.getString("motivo") : "");
                lista.add(t);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // ---------------------------------------------------------
    // PREFERENCIAS DEL HUÉSPED  (F-004 / F-011)
    // ---------------------------------------------------------
    private void guardarPreferencias(List<PreferenciaHuesped> lista) {
        if (lista == null) {
            return;
        }
        String sql = "INSERT INTO preferencia_huesped "
                + "(num_documento, tipo_preferencia, detalle, fecha_registro) "
                + "VALUES (?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (PreferenciaHuesped p : lista) {
                // Solo insertamos las preferencias nuevas (id == 0). Evita duplicados.
                if (p.getIdPreferencia() != 0) {
                    continue;
                }
                ps.setString(1, p.getNumDocumentoHuesped());
                ps.setString(2, p.getTipoPreferencia());
                ps.setString(3, p.getDetalle());
                ps.setTimestamp(4, Timestamp.valueOf(p.getFechaRegistro()));
                ps.executeUpdate();
                try (ResultSet gk = ps.getGeneratedKeys()) {
                    if (gk.next()) {
                        p.setIdPreferencia(gk.getInt(1));
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private List<PreferenciaHuesped> cargarPreferencias() {
        List<PreferenciaHuesped> lista = new ArrayList<>();
        String sql = "SELECT * FROM preferencia_huesped";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("fecha_registro");
                LocalDateTime fecha = (ts != null) ? ts.toLocalDateTime() : LocalDateTime.now();
                PreferenciaHuesped p = new PreferenciaHuesped(
                        rs.getString("num_documento"),
                        rs.getString("tipo_preferencia"),
                        rs.getString("detalle"),
                        fecha
                );
                p.setIdPreferencia(rs.getInt("id_preferencia"));
                lista.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // ---------------------------------------------------------
    // PAGO DE SERVICIOS CONTRATADOS  (F-010)
    // ---------------------------------------------------------
    private void guardarPagosServicio(List<PagoServicio> lista) {
        if (lista == null) {
            return;
        }
        String sql = "INSERT INTO pago_servicio "
                + "(num_documento, servicio, metodo_pago, monto, comprobante, fecha_pago, id_ficha) "
                + "VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (PagoServicio p : lista) {
                if (p.getIdPago() != 0) {
                    continue; // solo pagos nuevos
                }
                ps.setString(1, p.getNumDocumentoHuesped());
                ps.setString(2, p.getServicio());
                ps.setString(3, p.getMetodoPago());
                ps.setDouble(4, p.getMonto());
                ps.setString(5, p.getComprobante());
                ps.setTimestamp(6, Timestamp.valueOf(p.getFechaPago()));
                ps.setString(7, p.getIdFicha());
                ps.executeUpdate();
                try (ResultSet gk = ps.getGeneratedKeys()) {
                    if (gk.next()) {
                        p.setIdPago(gk.getInt(1));
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private List<PagoServicio> cargarPagosServicio() {
        List<PagoServicio> lista = new ArrayList<>();
        String sql = "SELECT * FROM pago_servicio";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("fecha_pago");
                LocalDateTime fecha = (ts != null) ? ts.toLocalDateTime() : LocalDateTime.now();
                PagoServicio p = new PagoServicio(
                        rs.getString("num_documento"),
                        rs.getString("servicio"),
                        rs.getString("metodo_pago"),
                        rs.getDouble("monto"),
                        rs.getString("comprobante"),
                        fecha
                );
                p.setIdPago(rs.getInt("id_pago"));
                p.setIdFicha(rs.getString("id_ficha"));
                lista.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }
}
