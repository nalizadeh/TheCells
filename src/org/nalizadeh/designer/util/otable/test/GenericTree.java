/*
 * Copyright 2007 N.A.J. nalizadeh.org - All rights reserved. nalizadeh.org
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */

package org.nalizadeh.designer.util.otable.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright: Copyright (c) 2007 N.A.J</p>
 *
 * <p>Organisation:</p>
 *
 * @author   Nader Alizadeh
 * @version  1.0
 */
public class GenericTree implements Serializable {

	private final Object partner;
	private final List<VKT> vkts;

	/**
	 * @param
	 *
	 * @return
	 */
	public GenericTree() {
		this("");
	}

	/**
	 * @param
	 *
	 * @return
	 */
	public GenericTree(Object partner) {
		this.partner = partner;
		this.vkts = new ArrayList<VKT>();
	}

	public GenericTree(GenericTree baum) {

		List<VKT> vkts = new ArrayList<VKT>();
		for (VKT vkt : baum.getVKTs()) {

			List<VK> vks = new ArrayList<VK>();
			for (VK vk : vkt.getVKs()) {

				List<VOT> vots = new ArrayList<VOT>();
				for (VOT vot : vk.getVOTs()) {

					List<VO> vos = new ArrayList<VO>();
					for (VO vo : vot.getVOs()) {
						vos.add(new VO(null, vo.getData()));
					}
					vots.add(new VOT(null, vot.getData(), vos));
				}
				vks.add(new VK(null, vk.getData(), vots));
			}
			vkts.add(new VKT(this, vkt.getData(), vks));
		}

		this.partner = (String) baum.getData();
		this.vkts = new ArrayList<VKT>(vkts);
	}

	/**
	 * @param
	 *
	 * @return
	 */
	public Object getData() {
		return partner;
	}

	/**
	 * @param
	 *
	 * @return
	 */
	public List<VKT> getVKTs() {
		return vkts;
	}

	/**
	 * @param
	 *
	 * @return
	 */
	public VKT addVKT(Object vkt) {
		VKT vktNeu = new VKT(partner, vkt);
		if (!vkts.contains(vktNeu)) {
			vkts.add(vktNeu);
			return vktNeu;
		}
		return (VKT) vkts.get(vkts.indexOf(vktNeu));
	}

	/**
	 * @param
	 *
	 * @return
	 */
	public VKT addVKT(VKT vkt) {
		return addVKT(vkt.getData());
	}

	/**
	 * @param
	 *
	 * @return
	 */
	public VK addVK(VK vk) {
		return addVKT(vk.getType()).addVK(vk.getData());
	}

	/**
	 * @param
	 *
	 * @return
	 */
	public VOT addVOT(VOT vot) {
		return addVK(vot.getVK()).addVOT(vot.getData());
	}

	/**
	 * @param
	 *
	 * @return
	 */
	public VO addVO(VO vo) {
		return addVOT(vo.getTyp()).addVO(vo.getData());
	}

	/**
	 * @param
	 *
	 * @return
	 */
	public boolean isEmpty() {
		return "".equals(partner);
	}

	/**
	 * @param
	 *
	 * @return
	 */
	public boolean hatVertrag() {
		return !vkts.isEmpty();
	}

	/**
	 * @param
	 *
	 * @return
	 */
	public void clear() {
		vkts.clear();
	}

	/**
	 * @param
	 *
	 * @return
	 */
	public void print() {
		System.out.println("==> " + partner.toString());
		for (VKT v : vkts) {
			v.print();
		}
	}

	//-----

	public static class VKT implements Serializable, Immutable, Comparable<VKT> {

		private final Object owner;
		private final Object data;
		private final List<VK> vks;

		/**
		 * @param
		 *
		 * @return
		 */
		public VKT(Object owner, Object data) {
			this.owner = owner;
			this.data = data;
			this.vks = new ArrayList<VK>();
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public VKT(Object owner, Object data, List<VK> vks) {

			List<VK> newVKS = new ArrayList<VK>();
			for (VK vk : vks) {
				newVKS.add(new VK(this, vk.getData(), vk.getVOTs()));
			}

			this.owner = owner;
			this.data = data;
			this.vks = new ArrayList<VK>(newVKS);
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public VK addVK(Object vk) {
			VK vkNeu = new VK(this, vk);
			if (!vks.contains(vkNeu)) {
				vks.add(vkNeu);
				return vkNeu;
			}
			return (VK) vks.get(vks.indexOf(vkNeu));
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public Object getOwner() {
			return owner;
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public Object getData() {
			return data;
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public List<VK> getVKs() {
			return vks;
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public int compareTo(VKT vkt) {
			return owner.toString().compareTo(vkt.owner.toString())
			+ data.toString().compareTo(vkt.data.toString());
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public boolean equals(Object vkt) {
			if (vkt instanceof VKT) {
				return doEqual((VKT) vkt);
			}
			return false;
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public boolean doEqual(VKT vkt) {
			return owner.equals(vkt.owner) && data.equals(vkt.data);
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public int hashCode() {
			return owner.hashCode() + data.hashCode();
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public String toString() {
			return data.toString();
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public void print() {
			System.out.println("====> " + data);
			for (VK v : vks) {
				v.print();
			}
		}
	}

	public static class VK implements Serializable, Immutable, Comparable<VK> {

		private final VKT type;
		private final Object data;
		private final List<VOT> vots;

		/**
		 * @param
		 *
		 * @return
		 */
		public VK(VKT type, Object data) {
			this.type = type;
			this.data = data;
			this.vots = new ArrayList<VOT>();
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public VK(VKT type, Object data, List<VOT> vots) {

			List<VOT> newVOTS = new ArrayList<VOT>();
			for (VOT vot : vots) {
				newVOTS.add(new VOT(this, vot.getData(), vot.getVOs()));
			}

			this.type = type;
			this.data = data;
			this.vots = new ArrayList<VOT>(newVOTS);
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public VOT addVOT(Object vot) {
			VOT votNeu = new VOT(this, vot);
			if (!vots.contains(votNeu)) {
				vots.add(votNeu);
				return votNeu;
			}
			return (VOT) vots.get(vots.indexOf(votNeu));
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public VKT getType() {
			return type;
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public Object getData() {
			return data;
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public List<VOT> getVOTs() {
			return vots;
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public int compareTo(VK vk) {
			return type.compareTo(vk.type) + data.toString().compareTo(vk.data.toString());
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public boolean equals(Object vk) {
			if (vk instanceof VK) {
				return doEqual((VK) vk);
			}
			return false;
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public boolean doEqual(VK vk) {
			return type.equals(vk.type) && data.equals(vk.data);
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public int hashCode() {
			return type.hashCode() + data.hashCode();
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public String toString() {
			return data.toString();
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public void print() {
			System.out.println("======> " + data);
			for (VOT v : vots) {
				v.print();
			}
		}
	}

	public static class VOT implements Serializable, Immutable, Comparable<VOT> {

		private final VK vk;
		private final Object data;
		private final List<VO> vos;

		/**
		 * @param
		 *
		 * @return
		 */
		public VOT(VK vk, Object data) {
			this.vk = vk;
			this.data = data;
			this.vos = new ArrayList<VO>();
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public VOT(VK vk, Object data, List<VO> vos) {

			List<VO> newVOS = new ArrayList<VO>();

			for (VO vo : vos) {
				newVOS.add(new VO(this, vo.getData()));
			}

			this.vk = vk;
			this.data = data;
			this.vos = new ArrayList<VO>(newVOS);
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public VO addVO(Object vo) {
			VO voNeu = new VO(this, vo);
			if (!vos.contains(voNeu)) {
				vos.add(voNeu);
				return voNeu;
			}
			return (VO) vos.get(vos.indexOf(voNeu));
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public VK getVK() {
			return vk;
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public Object getData() {
			return data;
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public List<VO> getVOs() {
			return vos;
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public int compareTo(VOT vot) {
			return vk.compareTo(vot.vk) + data.toString().compareTo(vot.data.toString());
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public boolean equals(Object vot) {
			if (vot instanceof VOT) {
				return doEqual((VOT) vot);
			}
			return false;
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public boolean doEqual(VOT vot) {
			return vk.equals(vot.vk) && data.equals(vot.data);
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public int hashCode() {
			return vk.hashCode() + data.hashCode();
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public String toString() {
			return data.toString();
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public void print() {
			System.out.println("========> " + data);
			for (VO v : vos) {
				v.print();
			}
		}
	}

	public static class VO implements Serializable, Immutable, Comparable<VO> {

		private final VOT type;
		private final Object data;

		/**
		 * @param
		 *
		 * @return
		 */
		public VO(VOT type, Object data) {
			this.type = type;
			this.data = data;
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public Object getData() {
			return data;
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public VOT getTyp() {
			return type;
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public int compareTo(VO vo) {
			return type.compareTo(vo.type) + data.toString().compareTo(vo.data.toString());
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public boolean equals(Object vo) {
			if (vo instanceof VO) {
				return doEqual((VO) vo);
			}
			return false;
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public boolean doEqual(VO vo) {
			return type.equals(vo.type) && data.equals(vo.data);
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public int hashCode() {
			return type.hashCode() + data.hashCode();
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public String toString() {
			return data.toString();
		}

		/**
		 * @param
		 *
		 * @return
		 */
		public void print() {
			System.out.println("==========> " + data);
		}
	}

	/**
	 * @param
	 *
	 * @return
	 */
	public VO getVO(Object v) {
		for (VKT vkt : vkts) {
			for (VK vk : vkt.vks) {
				for (VOT vot : vk.vots) {
					for (VO vo : vot.vos) {
						if (vo.getData().equals(v)) {
							return vo;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param
	 *
	 * @return
	 */
	public static void main(String[] args) {
		GenericTree b = new GenericTree("Root");

		b.addVKT("VKT-1").addVK("VK-1").addVOT("VOT-1").addVO("VO-1");
		b.addVKT("VKT-1").addVK("VK-1").addVOT("VOT-1").addVO("VO-2");
		b.addVKT("VKT-1").addVK("VK-1").addVOT("VOT-1").addVO("VO-3");
		b.addVKT("VKT-1").addVK("VK-1").addVOT("VOT-1").addVO("VO-4");

		b.addVKT("VKT-1").addVK("VK-2").addVOT("VOT-1").addVO("VO-1");
		b.addVKT("VKT-1").addVK("VK-2").addVOT("VOT-1").addVO("VO-2");
		b.addVKT("VKT-1").addVK("VK-2").addVOT("Vot-2").addVO("VO-1");
		b.addVKT("VKT-1").addVK("VK-2").addVOT("Vot-2").addVO("VO-2");

		b.addVKT("VKT-1").addVK("VK-3").addVOT("VOT-1").addVO("VO-1");
		b.addVKT("VKT-1").addVK("VK-3").addVOT("Vot-2").addVO("VO-1");
		b.addVKT("VKT-1").addVK("VK-3").addVOT("VOT-1").addVO("VO-2");
		b.addVKT("VKT-1").addVK("VK-3").addVOT("Vot-3").addVO("VO-1");

		b.print();

		System.out.println("=================================");

		GenericTree b2 = new GenericTree(b);
		b2.print();
	}

	public interface Immutable {

	}
}
