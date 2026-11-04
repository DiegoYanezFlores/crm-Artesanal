<div align="center">

# 💎 CRM Artesanal

### Sistema de gestión empresarial para artesanos joyeros del Ecuador

![Java](https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Estado](https://img.shields.io/badge/estado-en%20desarrollo-yellow?style=for-the-badge)

</div>

---

## 📖 Descripción

**CRM Artesanal** es una plataforma de gestión empresarial para **artesanos, talleres y pequeños negocios** que fabrican y comercializan joyas en Ecuador.

El sistema centraliza en un solo lugar:

- La administración de **clientes, proveedores y artesanos**.
- El **inventario** de metales preciosos, piedras e insumos.
- El **control de producción** en el taller.
- Las **ventas y compras**.
- La **gestión financiera y contable**.
- La **facturación electrónica** (SRI).
- El **registro y seguimiento de operaciones** para el cumplimiento normativo (UAFE).

El resultado esperado es un **prototipo funcional, modular y extensible** que aplique los conocimientos de Desarrollo de Software a necesidades reales de un sector productivo.

---

## 🧩 Módulos

| Módulo | Descripción |
|---|---|
| 👤 **Cliente** | Registro y gestión de clientes |
| 🚚 **Proveedor** | Proveedores de metales, piedras e insumos |
| 🛠️ **Artesano** | Datos y asignaciones de los artesanos del taller |
| 📦 **Inventario** | Control de metales preciosos, piedras e insumos |
| ⚙️ **Producción** | Órdenes y seguimiento de fabricación de joyas |
| 💰 **Ventas** | Registro de ventas a clientes |
| 🛒 **Compras** | Registro de compras a proveedores |
| 📊 **Contabilidad** | Gestión financiera y contable |
| 🧾 **Facturación** | Facturación electrónica e integración con el SRI (SOAP) |
| ⚖️ **Cumplimiento** | Registro de operaciones UAFE y auditoría |
| 🔗 **Integración** | Clientes SOAP del SRI y servicios externos |
| 📈 **Reportes** | Reportes de gestión |
| 🔐 **Seguridad** | Autenticación y control de acceso |

---

## 🏗️ Arquitectura

- **Monolito modular**: una sola aplicación desplegable, dividida en módulos independientes.
- **Package by Feature**: cada módulo agrupa su propio `controller`, `service`, `repository`, `model`, `dto` y `mapper`.
- **Servicios REST** para la API del sistema y **SOAP** para la integración con el SRI.
- **Programación orientada a objetos** y principios **SOLID**.

```text
src/main/java/edu/uio/crmartesanal
├── config/            # Configuración global
├── shared/            # exception, response, enums, util
├── seguridad/
├── cliente/
├── proveedor/
├── artesano/
├── inventario/
├── produccion/
├── ventas/
├── compras/
├── contabilidad/
├── facturacion/
├── cumplimiento/
│   ├── uafe/
│   └── auditoria/
├── integracion/
│   ├── sri/soap/
│   └── externa/
└── reportes/

src/main/resources
├── db/migration/      # Scripts de base de datos
├── wsdl/              # Contratos SOAP del SRI
└── static/
```

Estructura típica de un módulo:

```text
cliente/
├── controller/   # Endpoints REST
├── service/      # Lógica de negocio
├── repository/   # Acceso a datos (Spring Data JPA)
├── model/        # Entidades
├── dto/          # Objetos de entrada/salida
└── mapper/       # Conversión entidad ↔ DTO
```

---

## 🛠️ Tecnologías

| Tecnología | Uso |
|---|---|
| Java 25 | Lenguaje principal |
| Spring Boot | Framework de la aplicación |
| Spring Web MVC / WebFlux | Servicios REST y clientes HTTP |
| Spring Data JPA | Persistencia |
| Bean Validation | Validación de datos |
| PostgreSQL | Base de datos |
| Maven | Gestión de dependencias y construcción |
| JUnit | Pruebas automatizadas |
| Git / GitHub | Control de versiones |

---

## 🚀 Instalación y ejecución

### Requisitos

- JDK 25
- PostgreSQL
- Git
- IntelliJ IDEA (recomendado)

### Pasos

1. **Clonar el repositorio**

   ```bash
   git clone https://github.com/DiegoYanezFlores/crm-Artesanal.git
   cd crm-Artesanal
   ```

2. **Crear la base de datos**

   ```sql
   CREATE DATABASE crm_artesanal;
   ```

3. **Configurar la conexión** en `src/main/resources/application.properties`

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/crm_artesanal
   spring.datasource.username=postgres
   spring.datasource.password=tu_password
   spring.jpa.hibernate.ddl-auto=update
   ```

4. **Ejecutar la aplicación**

   ```bash
   ./mvnw spring-boot:run
   ```

   En Windows: `mvnw.cmd spring-boot:run`

5. **Ejecutar las pruebas**

   ```bash
   ./mvnw test
   ```

---

## 🗓️ Plan de desarrollo

El proyecto se desarrolla de forma progresiva durante **dos meses**.

- [x] Creación del proyecto y estructura de paquetes
- [ ] Configuración de base de datos y módulo `shared`
- [ ] Módulos de cliente, proveedor y artesano
- [ ] Inventario y producción
- [ ] Ventas y compras
- [ ] Contabilidad y facturación electrónica (SRI – SOAP)
- [ ] Cumplimiento (UAFE) y auditoría
- [ ] Seguridad y reportes
- [ ] Pruebas, documentación y entrega del prototipo

---

## 🤝 Convenciones de trabajo

- Ramas: `main` (estable) y `feature/<modulo>` para nuevas funcionalidades.
- Commits descriptivos en español, por ejemplo: `feat(cliente): crear endpoint de registro`.
- Cada módulo debe incluir sus pruebas en `src/test/java/edu/uio/crmartesanal/<modulo>`.

---

<div align="center">

**Proyecto académico — Lenguaje de Programación 2**
Instituto Superior Tecnológico Quito · PAO II 2026

Docente: **MSc. Diego Yánez Flores**

</div>
