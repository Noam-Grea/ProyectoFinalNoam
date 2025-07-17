package vista;

import viewmodel.InventarioViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class PanelDashboard extends JPanel {

    private InventarioViewModel viewModel;

    public PanelDashboard(InventarioViewModel viewModel) {
        this.viewModel = viewModel;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(crearResumen(), BorderLayout.NORTH);
        add(crearGrafico(), BorderLayout.CENTER);
    }

    private JPanel crearResumen() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 20, 20));
        panel.setBorder(BorderFactory.createTitledBorder("Resumen general"));

        JLabel lblProd = new JLabel("📦 Productos: " + viewModel.getTotalProductos(), JLabel.CENTER);
        JLabel lblCli = new JLabel("👥 Clientes: " + viewModel.getTotalClientes(), JLabel.CENTER);
        JLabel lblProv = new JLabel("🏢 Proveedores: " + viewModel.getTotalProveedores(), JLabel.CENTER);
        JLabel lblLow = new JLabel("⚠ Bajo stock: " + viewModel.getProductosBajoStock(), JLabel.CENTER);

        Font f = new Font("SansSerif", Font.BOLD, 16);
        for (JLabel l : new JLabel[]{lblProd, lblCli, lblProv, lblLow}) l.setFont(f);

        panel.add(lblProd);
        panel.add(lblCli);
        panel.add(lblProv);
        panel.add(lblLow);

        return panel;
    }

    private JPanel crearGrafico() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Integer> datos = viewModel.getStockPorAlmacen();

        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Distribución del Stock por Almacén",
                dataset,
                true, true, false
        );

        return new ChartPanel(chart);
    }
    public void refrescar() {
    removeAll();  // Enlève les composants précédents
    setLayout(new BorderLayout());
    add(crearResumen(), BorderLayout.NORTH);
    add(crearGrafico(), BorderLayout.CENTER);
    revalidate();
    repaint();
}
}