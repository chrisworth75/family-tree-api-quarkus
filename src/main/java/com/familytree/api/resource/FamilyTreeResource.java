package com.familytree.api.resource;

import com.familytree.api.dto.AddMemberRequest;
import com.familytree.api.dto.CreateTreeRequest;
import com.familytree.api.entity.FamilyTree;
import com.familytree.api.entity.Member;
import com.familytree.api.entity.Relationship;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/api/trees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FamilyTreeResource {

    @POST
    @Transactional
    public Response createTree(CreateTreeRequest request) {
        FamilyTree tree = new FamilyTree();
        tree.name = request.name;
        tree.createdBy = request.createdBy;
        tree.persist();

        if (request.rootMember != null) {
            Member rootMember = new Member();
            rootMember.familyTree = tree;
            rootMember.firstName = request.rootMember.firstName;
            rootMember.lastName = request.rootMember.lastName;
            rootMember.birthDate = request.rootMember.birthDate != null ?
                LocalDate.parse(request.rootMember.birthDate) : null;
            rootMember.deathDate = request.rootMember.deathDate != null ?
                LocalDate.parse(request.rootMember.deathDate) : null;
            rootMember.gender = request.rootMember.gender;
            rootMember.isAlive = request.rootMember.isAlive;
            rootMember.persist();
        }

        return Response.status(Response.Status.CREATED).entity(tree).build();
    }

    @GET
    public List<FamilyTree> getAllTrees() {
        return FamilyTree.listAll();
    }

    @GET
    @Path("/{treeId}")
    public Response getTree(@PathParam("treeId") Long treeId) {
        FamilyTree tree = FamilyTree.findById(treeId);
        if (tree == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(tree).build();
    }

    @DELETE
    @Path("/{treeId}")
    @Transactional
    public Response deleteTree(@PathParam("treeId") Long treeId) {
        FamilyTree tree = FamilyTree.findById(treeId);
        if (tree == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        tree.delete();
        return Response.noContent().build();
    }

    @POST
    @Path("/{treeId}/members")
    @Transactional
    public Response addMember(@PathParam("treeId") Long treeId, AddMemberRequest request) {
        FamilyTree tree = FamilyTree.findById(treeId);
        if (tree == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Member member = new Member();
        member.familyTree = tree;
        member.firstName = request.firstName;
        member.lastName = request.lastName;
        member.birthDate = request.birthDate != null ? LocalDate.parse(request.birthDate) : null;
        member.deathDate = request.deathDate != null ? LocalDate.parse(request.deathDate) : null;
        member.gender = request.gender;
        member.isAlive = request.isAlive;
        member.persist();

        return Response.status(Response.Status.CREATED).entity(member).build();
    }

    @GET
    @Path("/{treeId}/members")
    public Response getMembers(@PathParam("treeId") Long treeId) {
        FamilyTree tree = FamilyTree.findById(treeId);
        if (tree == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(tree.members).build();
    }

    @GET
    @Path("/{treeId}/members/{memberId}")
    public Response getMember(@PathParam("treeId") Long treeId, @PathParam("memberId") Long memberId) {
        Member member = Member.findById(memberId);
        if (member == null || !member.familyTree.id.equals(treeId)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(member).build();
    }

    @POST
    @Path("/{treeId}/members/{memberId}/partner")
    @Transactional
    public Response addPartner(@PathParam("treeId") Long treeId,
                                @PathParam("memberId") Long memberId,
                                Map<String, Long> request) {
        Member member1 = Member.findById(memberId);
        Member member2 = Member.findById(request.get("partnerId"));

        if (member1 == null || member2 == null || !member1.familyTree.id.equals(treeId)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Relationship relationship = new Relationship();
        relationship.member1 = member1;
        relationship.member2 = member2;
        relationship.relationshipType = Relationship.RelationshipType.PARTNER;
        relationship.persist();

        return Response.status(Response.Status.CREATED).entity(relationship).build();
    }

    @POST
    @Path("/{treeId}/members/{memberId}/children")
    @Transactional
    public Response addChild(@PathParam("treeId") Long treeId,
                             @PathParam("memberId") Long memberId,
                             Map<String, Long> request) {
        Member parent = Member.findById(memberId);
        Member child = Member.findById(request.get("childId"));

        if (parent == null || child == null || !parent.familyTree.id.equals(treeId)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Relationship parentRel = new Relationship();
        parentRel.member1 = parent;
        parentRel.member2 = child;
        parentRel.relationshipType = Relationship.RelationshipType.PARENT;
        parentRel.persist();

        Relationship childRel = new Relationship();
        childRel.member1 = child;
        childRel.member2 = parent;
        childRel.relationshipType = Relationship.RelationshipType.CHILD;
        childRel.persist();

        Map<String, Object> response = new HashMap<>();
        response.put("parentRelationship", parentRel);
        response.put("childRelationship", childRel);

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @Path("/{treeId}/members/{memberId}/relationships")
    public Response getRelationships(@PathParam("treeId") Long treeId,
                                     @PathParam("memberId") Long memberId) {
        Member member = Member.findById(memberId);
        if (member == null || !member.familyTree.id.equals(treeId)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Map<String, Object> relationships = new HashMap<>();
        relationships.put("asFirstMember", member.relationshipsAsMember1);
        relationships.put("asSecondMember", member.relationshipsAsMember2);

        return Response.ok(relationships).build();
    }
}
