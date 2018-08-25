package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.ListSelectionModel;

/**
 * Classe que representa a GUI de um álbum.
 */
public class ViewAlbum extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldPath;
	private JTextField textFieldIdNode;
	private JTextField textFieldImageName;
	private JButton btnConectar;
	private JButton btnBuscar;
	private JButton btnSalvar;
	private JButton btnDesconectar;
	private JLabel lblStatusDht;
	private JLabel lblImage;
	private JMenuItem mntmCarregarImagem;
	private JList list;
	private JLabel lblImagensArmazenadosNeste;
	private JLabel lblProximo;
	private JTextField txtNextnode;
	private JLabel lblAnterior;
	private JTextField txtPrevnode;
	private JButton btnCarregar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ViewAlbum frame = new ViewAlbum();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ViewAlbum() {
		setTitle("Álbum de Fotos");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1045, 621);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnArquivo = new JMenu("Arquivo");
		menuBar.add(mnArquivo);
		
		mntmCarregarImagem = new JMenuItem("Carregar Imagem...");
		mnArquivo.add(mntmCarregarImagem);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panelPicture = new JPanel();
		panelPicture.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelPicture.setBounds(34, 119, 766, 396);
		contentPane.add(panelPicture);
		panelPicture.setLayout(null);
		
		lblImage = new JLabel("");
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblImage.setBounds(10, 11, 746, 374);
		panelPicture.add(lblImage);
		
		btnSalvar = new JButton("Salvar");
		btnSalvar.setEnabled(false);
		btnSalvar.setBounds(386, 527, 89, 23);
		contentPane.add(btnSalvar);
		
		textFieldPath = new JTextField();
		textFieldPath.setText(".\\initxt.txt");
		textFieldPath.setBounds(147, 14, 162, 20);
		contentPane.add(textFieldPath);
		textFieldPath.setColumns(10);
		
		JLabel lblArquivoInicial = new JLabel("Arquivo Inicial :");
		lblArquivoInicial.setBounds(34, 17, 89, 14);
		contentPane.add(lblArquivoInicial);
		
		JLabel lblIdDoN = new JLabel("Nó (IP; Porta; Hash):");
		lblIdDoN.setBounds(846, 17, 148, 14);
		contentPane.add(lblIdDoN);
		
		textFieldIdNode = new JTextField();
		textFieldIdNode.setEditable(false);
		textFieldIdNode.setBounds(846, 42, 173, 20);
		contentPane.add(textFieldIdNode);
		textFieldIdNode.setColumns(10);
		
		JLabel lblNomeDaImagem = new JLabel("Nome da Imagem :");
		lblNomeDaImagem.setBounds(380, 95, 148, 14);
		contentPane.add(lblNomeDaImagem);
		
		textFieldImageName = new JTextField();
		textFieldImageName.setBounds(517, 92, 167, 20);
		contentPane.add(textFieldImageName);
		textFieldImageName.setColumns(10);
		
		btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnBuscar.setBounds(702, 91, 98, 23);
		contentPane.add(btnBuscar);
		
		JLabel lblDht = new JLabel("Status da DHT :");
		lblDht.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDht.setBounds(10, 42, 127, 33);
		contentPane.add(lblDht);
		
		lblStatusDht = new JLabel("Desconectado");
		lblStatusDht.setBackground(Color.WHITE);
		lblStatusDht.setHorizontalAlignment(SwingConstants.LEFT);
		lblStatusDht.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblStatusDht.setBounds(147, 47, 416, 23);
		contentPane.add(lblStatusDht);
		
		btnConectar = new JButton("Conectar");
		
		btnConectar.setBounds(312, 13, 117, 23);
		contentPane.add(btnConectar);
		
		btnDesconectar = new JButton("Desconectar");
		btnDesconectar.setEnabled(false);
		btnDesconectar.setBounds(446, 13, 117, 23);
		contentPane.add(btnDesconectar);
		
		JLabel lblVisualizaoDaImagem = new JLabel("Visualização da Imagem:");
		lblVisualizaoDaImagem.setHorizontalAlignment(SwingConstants.CENTER);
		lblVisualizaoDaImagem.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblVisualizaoDaImagem.setBounds(10, 85, 200, 29);
		contentPane.add(lblVisualizaoDaImagem);
		
		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBounds(810, 218, 209, 297);
		contentPane.add(list);
		
		lblImagensArmazenadosNeste = new JLabel("Dados armazenados neste nó:");
		lblImagensArmazenadosNeste.setHorizontalAlignment(SwingConstants.CENTER);
		lblImagensArmazenadosNeste.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblImagensArmazenadosNeste.setBounds(810, 184, 209, 23);
		contentPane.add(lblImagensArmazenadosNeste);
		
		lblProximo = new JLabel("Próximo:");
		lblProximo.setBounds(846, 73, 109, 14);
		contentPane.add(lblProximo);
		
		txtNextnode = new JTextField();
		txtNextnode.setEditable(false);
		txtNextnode.setBounds(846, 91, 173, 20);
		contentPane.add(txtNextnode);
		txtNextnode.setColumns(10);
		
		lblAnterior = new JLabel("Anterior:");
		lblAnterior.setBounds(846, 122, 89, 14);
		contentPane.add(lblAnterior);
		
		txtPrevnode = new JTextField();
		txtPrevnode.setEditable(false);
		txtPrevnode.setBounds(846, 144, 173, 20);
		contentPane.add(txtPrevnode);
		txtPrevnode.setColumns(10);
		
		btnCarregar = new JButton("Carregar...");
		btnCarregar.setBounds(220, 91, 89, 23);
		contentPane.add(btnCarregar);
		
		
		addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
            	if(!btnConectar.isEnabled())
            		btnDesconectar.doClick();
                e.getWindow().dispose();
            }
        });
	}
	
	public void addConectaListener(ActionListener action) {
		btnConectar.addActionListener(action);
	}
	
	public void addDesconectaListener(ActionListener action) {
		btnDesconectar.addActionListener(action);
	}
	
	public void addBuscaListener(ActionListener action) {
		btnBuscar.addActionListener(action);
	}
	
	public void addSalvaListener(ActionListener action) {
		btnSalvar.addActionListener(action);
	}
	
	public void addCarregarImgListener(ActionListener action) {
		mntmCarregarImagem.addActionListener(action);
		btnCarregar.addActionListener(action);
	}
	
	public void addChangeListener(PropertyChangeListener listener) {
		lblStatusDht.addPropertyChangeListener(listener);
	}
	
	
	/**
	 * Define a imagem que aparecerá no quadro de visualização.
	 * <br> Altera a escala da imagem para caber no quadro
	 * @param icon
	 */
	public void setImg(ImageIcon icon) {
		ImageIcon resized = new ImageIcon(icon.getImage().getScaledInstance(lblImage.getWidth(), lblImage.getHeight(), 0));
		lblImage.setIcon(resized);
	}
	
	/**
	 * Define o status do botão Salvar.
	 * @param b boolean
	 */
	public void setBtnSalvar(boolean b) {
		btnSalvar.setEnabled(b);
	}
	
	/**
	 * Define o status do botão Conectar.
	 * @param b boolean
	 */
	public void setBtnConectar(boolean b) {
		btnConectar.setEnabled(b);
	}
	
	/**
	 * 
	 * @return path caminho do arquivo inicial contendo os IP's.
	 */
	public String getPath() {
		return textFieldPath.getText();
	}
	
	/**
	 * Define o status da conexão DHT.
	 * @param status
	 */
	public void setStatus(String status) {
		lblStatusDht.setText(status);
	}
	/**
	 * Define o id do nó atual 
	 * @param id
	 */
	public void setIdNode(String id) {
		textFieldIdNode.setText(id);
	}
	
	/**
	 * Define ativação do botão Desconecta
	 * @param enable
	 */
	public void setBtnDesconecta(boolean enable) {
		btnDesconectar.setEnabled(enable);
	}

	public String getTxtNextnode() {
		return txtNextnode.getText();
	}

	public void setTxtNextnode(String text) {
		this.txtNextnode.setText(text);
	}

	public String getTxtPrevnode() {
		return txtPrevnode.getText();
	}

	public void setTxtPrevnode(String txtPrevnode) {
		this.txtPrevnode.setText(txtPrevnode);
	}
	
	
	
}
