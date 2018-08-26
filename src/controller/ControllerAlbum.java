package controller;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ExportException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import dht.Node;
import dht.SHA1;
import gui.ViewAlbum;
import model.Picture;

/**
 * Classe que controla a instância view do álbum.
 */
public class ControllerAlbum {

	private ViewAlbum view;
	private Picture picture;
	private Node node;

	public ControllerAlbum(ViewAlbum view, Node node) {
		this.view = view;
		this.node = node;
		this.picture = new Picture();
		view.setIdNode(node.toString());

		// Adiciona os listeners da view
		view.addConectaListener(new ConectaListener());
		view.addDesconectaListener(new DesconectaListener());
		view.addCarregarImgListener(new CarregaImgListener());
		view.addWindowFocusListener(new WindowFocusListenerImpl());
		view.addAtualizaListener(new AtualizaListener());
		view.addSalvaListener(new SalvaAction());
		view.addBtnCalcHash(new HashNameImg());
		view.addBtnLimpar(new LimpaActionListener());
		view.addClickListener(new ClickListenerList());
		view.addBtnBusca(new BuscaListener());
		view.getBtnFile().addActionListener(new InitFileChooserAction());
		view.getTextFieldPath().setText(System.getProperty("user.dir")+"\\initxt.txt");
		view.addWindowlistenerClose(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					
					if (node.getDht().isSuperNode() && JOptionPane.showConfirmDialog(null,
							"Este é o nó de referência. " + "Finalize este nó por último.\n "
									+ "Outros nós procuram por este nó para se conectarem, deseja mesmo finaliza-lo?",
							"WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						
						if (!view.getBtnConectar().isEnabled()) {
							view.getBtnDesconectar().doClick();
						}
						e.getWindow().dispose();
					} else {
						if (node.getDht().isSuperNode()) {
							view.doNothingOnClosing();
						}else {
						if (!view.getBtnConectar().isEnabled()) {
							view.getBtnDesconectar().doClick();
						}
						e.getWindow().dispose();
						}
					}
				} catch (HeadlessException | RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
	
	class InitFileChooserAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jf = new JFileChooser();
			jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivo de texto", "txt", "text");
			jf.setFileFilter(filter);
			jf.setCurrentDirectory(new java.io.File("."));
			int status = jf.showOpenDialog(view.getContentPane());
			if (status == JFileChooser.APPROVE_OPTION) {
				view.getTextFieldPath().setText(jf.getSelectedFile().getPath());
			}
			
		}
		
	}

	/**
	 * Dispara uma busca na DHT
	 */
	class BuscaListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e2) {
			try {
				String key = "";
				if (view.getTxtHashImg().isEmpty() && !view.getTextFieldImageName().getText().isEmpty())
					view.getBtnCalchash().doClick();
				else if (view.getTxtHashImg().isEmpty() && view.getTextFieldImageName().getText().isEmpty()) {
					throw new IOException();
				}

				key = view.getTxtHashImg();

				node.getDht().setFounded(false);
				node.getDht().setNotFounded(false);
				node.getDht().retrieve(key);
				view.getLblStatusDaImagem().setText("Buscando Imagem...");

				new Thread(() -> {
					int count = 0;
					while (count < 120) {
						try {
							if ((node.getDht().isConnected() && node.getDht().isInserted()) || node.getDht().isStoped()
									|| node.getDht().isFounded() || node.getDht().isNotFounded())
								break;
							view.getLblStatusDaImagem().setText(node.getDht().getStatus());
							count++;
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
						} catch (RemoteException e1) {
							break;
						}
					}

					try {
						if (node.getDht().isFounded()) {
							view.getLblStatusDaImagem().setText("Imagem encontrada!");
							Picture pic = (Picture) node.deserialize(node.getDht().getResult());
							view.setImg(new ImageIcon(pic.getImg()));
							view.getBtnLimpar().setEnabled(true);
						} else {
							view.getLblStatusDaImagem().setText("Imagem não encontrada.");
							JOptionPane.showMessageDialog(view.getContentPane(), "Imagem não encontrada. ", // mensagem
									"Busca sem resultado", // titulo da janela
									JOptionPane.INFORMATION_MESSAGE);
						}
					} catch (RemoteException e) {
						JOptionPane.showMessageDialog(view.getContentPane(),
								"Erro ao Carregar Imagem: " + e.getMessage(), // mensagem
								"Error: Carregar resultado da busca.", // titulo da janela
								JOptionPane.ERROR_MESSAGE);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(view.getContentPane(),
								"Erro ao Carregar Imagem: " + e.getMessage(), // mensagem
								"Error: Carregar resultado da busca.", // titulo da janela
								JOptionPane.ERROR_MESSAGE);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}).start();

			} catch (RemoteException | NotBoundException e) {
				JOptionPane.showMessageDialog(view.getContentPane(), "Erro ao Buscar Imagem: " + e.getMessage(), // mensagem
						"Error: Buscar imagem", // titulo da janela
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(view.getContentPane(), "Erro : O campo ID não pode estar vazio", // mensagem
						"Error: Buscar imagem", // titulo da janela
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	/**
	 * Implementa a ação de salvar uma imagem
	 */
	class SalvaAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {

				byte[] data = node.serialize(picture);

				String inputName = view.getTextFieldImageName().getText(), inputId = view.getTxtHashImg();
				picture.setId(inputId);
				picture.setName(inputName);
				node.getDht().store(picture.getId(), data);

				resetImg();
				view.setImg(new ImageIcon("Imagem foi Salva."));

			} catch (RemoteException e) {
				JOptionPane.showMessageDialog(view.getContentPane(), "Erro ao Salvar Imagem: " + e.getMessage(), // mensagem
						"Error: Salvar imagem", // titulo da janela
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} catch (NotBoundException e) {
				JOptionPane.showMessageDialog(view.getContentPane(), "Erro ao Salvar Imagem: " + e.getMessage(), // mensagem
						"Error: Salvar imagem", // titulo da janela
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(view.getContentPane(), "Erro ao Salvar Imagem: " + e.getMessage(), // mensagem
						"Error: Salvar imagem", // titulo da janela
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}

		}

	}

	/**
	 * Carrega uma imagem da lista ao clicar duas vezes.
	 */
	class ClickListenerList implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				picture = view.getList().getSelectedValue();

				view.getBtnCalchash().setEnabled(true);
				view.setTxtHashImg(picture.getId());
				view.getTextFieldImageName().setText(picture.getName());
				try {
					view.setImg(new ImageIcon(picture.getImg()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				view.getBtnLimpar().setEnabled(true);
			}

		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}
	}

	/**
	 * Reseta a imagem da tela
	 */
	public void resetImg() {
		view.setImg(new ImageIcon("Imagem."));
		view.getTextFieldImageName().setText("");
		view.setTxtHashImg("");
		view.setBtnSalvar(false);
		// view.getBtnCalchash().setEnabled(false);
		view.getBtnLimpar().setEnabled(false);
		updateTextFields();
	}

	/**
	 * Atualiza os atributos do nó na tela.
	 */
	public void updateTextFields() {
		try {
			if (node.getNext() != null)
				view.setTxtNextnode(node.getNext().getNode().toString());
			if (node.getPrev() != null)
				view.setTxtPrevnode(node.getPrev().getNode().toString());
			view.setListImg(loadListPictures());
		} catch (RemoteException e) {
		}
	}

	/**
	 * Atualiza os campos ao receber eventos
	 */
	class AtualizaListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			updateTextFields();
		}
	}

	/**
	 * Atualiza Campos ao voltar o foco na janela
	 */
	class WindowFocusListenerImpl implements java.awt.event.WindowFocusListener {
		@Override
		public void windowGainedFocus(WindowEvent e) {
			updateTextFields();
		}

		@Override
		public void windowLostFocus(WindowEvent e) {
			updateTextFields();
		}
	}

	/**
	 * Classe que implementa as ações ao pressionar o botão "Conecta".
	 * 
	 */
	class ConectaListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// atualiza o status da dht enquanto tenta-se conectar
			try {
				node.getDht().setStoped(false);
			} catch (RemoteException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}

			/*
			 * Inicia uma thread para acompanhar a atualização do status da dht
			 */
			new Thread(() -> {
				int count = 0;
				while (count < 120) {
					try {
						if ((node.getDht().isConnected() && node.getDht().isInserted()) || node.getDht().isStoped())
							break;
						view.setStatus(node.getDht().getStatus());
						count++;
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					} catch (RemoteException e1) {
						break;
					}
				}
				try {
					if (node.getNext() != null)
						view.setTxtNextnode(node.getNext().getNode().toString());
					if (node.getPrev() != null)
						view.setTxtPrevnode(node.getPrev().getNode().toString());
				} catch (RemoteException e1) {
				}
			}).start();

			/*
			 * Inicia a operação join em uma thread separada para não travar a interface do
			 * usuário
			 */
			new Thread(() -> {

				try {
					view.setBtnConectar(false);
					view.setBtnDesconecta(true);
					node.getDht().join(view.getPath());
					view.setStatus(node.getDht().getStatus());
					view.setIdNode(node.toString());
					view.setBtnDesconecta(true);
					view.getBtnAtualizar().setEnabled(true);
					view.getBtnCarregar().setEnabled(true);
					view.getBtnBuscar().setEnabled(true);
					view.getTxtHashimg().setEditable(true);
					view.getTextFieldImageName().setEditable(true);
					view.getBtnCalchash().setEnabled(true);
				} catch (ConnectException e1) {
					view.setBtnDesconecta(false);
					JOptionPane.showMessageDialog(view.getContentPane(),
							"Erro ao iniciar conexão: " + e1.getMessage() + "\n Tente (re)iniciar o rmiregistry ", // mensagem
							"Error: join DHT", // titulo da janela
							JOptionPane.ERROR_MESSAGE);
					try {
						view.setBtnConectar(true);
						view.setStatus("Erro: não foi possível conectar.");
						node.getDht().setStoped(true);
					} catch (RemoteException e2) {
					}
					e1.printStackTrace();
				} catch (AlreadyBoundException e1) {
					view.setBtnDesconecta(false);
					JOptionPane.showMessageDialog(view.getContentPane(),
							"Erro ao conectar à rede DHT: Nó já está registrado.\n" + e1.getMessage(), // mensagem
							"Error: join DHT", // titulo da janela
							JOptionPane.ERROR_MESSAGE);
					try {
						view.setBtnConectar(true);
						view.setStatus("Erro: não foi possível conectar.");
						node.getDht().setStoped(true);
					} catch (RemoteException e2) {
					}
					e1.printStackTrace();
				} catch (ExportException e1) {
					JOptionPane.showMessageDialog(view.getContentPane(),
							"Objeto já está registrado: " + e1.getMessage(), // mensagem
							"Error: join DHT", // titulo da janela
							JOptionPane.ERROR_MESSAGE);
					try {
						view.setBtnConectar(true);
						view.setStatus("Erro: não foi possível conectar.");
						node.getDht().setStoped(true);
					} catch (RemoteException e2) {
					}
					e1.printStackTrace();
				} catch (IOException e1) {
					view.setBtnDesconecta(false);
					JOptionPane.showMessageDialog(view.getContentPane(),
							"Erro ao ler o arquivo inicial: " + e1.getMessage(), // mensagem
							"Error: join DHT", // titulo da janela
							JOptionPane.ERROR_MESSAGE);
					try {
						view.setBtnConectar(true);
						view.setStatus("Erro: não foi possível conectar.");
						node.getDht().setStoped(true);
					} catch (RemoteException e2) {
					}
					e1.printStackTrace();
				}
			}).start();

		}

	}

	/**
	 * Classe que implementa as ações ao pressionar o botão "Desconectar".
	 */
	class DesconectaListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				node.getDht().leave();
				node.getDht().setStoped(true);
				view.setStatus("Desconectado.");
				view.setBtnDesconecta(false);
				view.setBtnConectar(true);
				updateTextFields();
				view.getBtnAtualizar().setEnabled(false);
				view.getBtnBuscar().setEnabled(false);
				view.getBtnCarregar().setEnabled(false);
				view.getBtnCalchash().setEnabled(false);

			} catch (RemoteException e1) {
				JOptionPane.showMessageDialog(view.getContentPane(), "Erro ao desconectar à rede DHT ", // mensagem
						"Error: leave DHT", // titulo da janela
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			} catch (NotBoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Carrega uma imagem a ser salva na DHT. <br>
	 * Ação disparada ao pressionar o botão "Arquivo > Carregar Imagem..."
	 */
	class CarregaImgListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivo de imagens", "jpg", "jpeg");
			fileChooser.setFileFilter(filter);
			int status = fileChooser.showOpenDialog(view.getContentPane());
			if (status == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				BufferedImage bufferImg = null;
				try {
					bufferImg = ImageIO.read(file);
					picture.setImg(Picture.imageToByteArray(bufferImg));
					picture.setName(file.getName());
					picture.setDate(new Date().toString());
					picture.setId(SHA1.digest(picture.getName()));
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(view.getContentPane(), "Erro ao carregar imagem.", // mensagem
							"Imagem não foi carregada", // titulo da janela
							JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				} catch (NoSuchAlgorithmException e1) {
					JOptionPane.showMessageDialog(view.getContentPane(), "Erro ao criar hash: " + e1.getMessage(), // mensagem
							"Error: hash", // titulo da janela
							JOptionPane.ERROR_MESSAGE);
				}

				view.getBtnCalchash().setEnabled(true);
				view.setTxtHashImg(picture.getId());
				view.getTextFieldImageName().setText(picture.getName());
				try {
					view.setImg(new ImageIcon(picture.getImg()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				view.setBtnSalvar(true);
				view.getBtnLimpar().setEnabled(true);

			}
		}
	}

	class LimpaActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			resetImg();
		}
	}

	private AbstractListModel<Picture> loadListPictures() throws RemoteException {

		List<Picture> listPictures = new ArrayList<Picture>();
		for (Entry<String, byte[]> data : node.getData().entrySet()) {
			try {
				Picture pic = (Picture) node.deserialize(data.getValue());
				pic.setId(data.getKey());
				listPictures.add(pic);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return new AbstractListModel<Picture>() {
			private static final long serialVersionUID = 1L;

			public int getSize() {
				return listPictures.size();
			}

			public Picture getElementAt(int index) {
				return listPictures.get(index);
			}
		};
	}

	/**
	 * Faz o hash do nome atual da imagem.
	 */
	class HashNameImg implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				if (!view.getTextFieldImageName().getText().isEmpty())
					picture.setId(SHA1.digest(view.getTextFieldImageName().getText()));
				else
					JOptionPane.showMessageDialog(view.getContentPane(), "Erro ao criar hash: Campo nome está vazio", // mensagem
							"Error: hash", // titulo da janela
							JOptionPane.ERROR_MESSAGE);
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				JOptionPane.showMessageDialog(view.getContentPane(), "Erro ao criar hash: " + e.getMessage(), // mensagem
						"Error: hash", // titulo da janela
						JOptionPane.ERROR_MESSAGE);
			}
			view.setTxtHashImg(picture.getId());
		}

	}

	/**
	 * @return the view
	 */
	public ViewAlbum getView() {
		return view;
	}

	/**
	 * @param view
	 *            the view to set
	 */
	public void setView(ViewAlbum view) {
		this.view = view;
	}

	/**
	 * @return the picture
	 */
	public Picture getPicture() {
		return picture;
	}

	/**
	 * @param picture
	 *            the picture to set
	 */
	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	/**
	 * @return the node
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * @param node
	 *            the node to set
	 */
	public void setNode(Node node) {
		this.node = node;
	}

}
