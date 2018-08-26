package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ViewMain extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnNovoAlbum;
	private JLabel lblHashGerado;
	private JLabel lblPorta;
	private JTextField txtHash;
	private JTextField txtPorta;
	private JCheckBox chckbxSha;
	private JTextField txtIp;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewMain frame = new ViewMain();
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
	public ViewMain() {
		setTitle("Iniciar aplicação");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 444, 234);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		btnNovoAlbum = new JButton("+ Novo Álbum");
		btnNovoAlbum.setToolTipText("Inicializa um novo álbum com o ID e porta");
		btnNovoAlbum.setBounds(42, 70, 342, 114);
		btnNovoAlbum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		contentPane.setLayout(null);
		btnNovoAlbum.setFont(new Font("Tahoma", Font.BOLD, 21));
		contentPane.add(btnNovoAlbum);
		
		lblHashGerado = new JLabel("Hash Gerado");
		lblHashGerado.setBounds(5, 11, 94, 14);
		contentPane.add(lblHashGerado);
		
		lblPorta = new JLabel("IP / Porta");
		lblPorta.setToolTipText("IP / Porta");
		lblPorta.setBounds(5, 39, 94, 20);
		contentPane.add(lblPorta);
		
		txtHash = new JTextField();
		txtHash.setText("Hash");
		txtHash.setBounds(111, 8, 211, 22);
		contentPane.add(txtHash);
		txtHash.setColumns(10);
		
		txtPorta = new JTextField();
		txtPorta.setText("1099");
		txtPorta.setBounds(236, 36, 86, 23);
		contentPane.add(txtPorta);
		txtPorta.setColumns(10);
		
		chckbxSha = new JCheckBox("SHA1");
		chckbxSha.setToolTipText("Faz o hash do ID");
		chckbxSha.setBounds(328, 7, 78, 23);
		contentPane.add(chckbxSha);
		
		txtIp = new JTextField();
		txtIp.setText("127.0.0.1");
		txtIp.setBounds(110, 36, 116, 23);
		contentPane.add(txtIp);
		txtIp.setColumns(10);
	}
	
	public JTextField getTxtIp() {
		return txtIp;
	}

	public void setTxtIp(JTextField txtIp) {
		this.txtIp = txtIp;
	}

	public void addBtnListener(ActionListener a) {
		btnNovoAlbum.addActionListener(a);
	}

	public JTextField getTxtPorta() {
		return txtPorta;
	}

	public void setTxtPorta(JTextField txtPorta) {
		this.txtPorta = txtPorta;
	}

	public JTextField getTxtHash() {
		return txtHash;
	}

	public void setTxtHash(JTextField txtHash) {
		this.txtHash = txtHash;
	}

	public JCheckBox getChckbxSha() {
		return chckbxSha;
	}

	public void setChckbxSha(JCheckBox chckbxSha) {
		this.chckbxSha = chckbxSha;
	}
}
